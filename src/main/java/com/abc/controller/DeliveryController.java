package com.abc.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.abc.domain.Item;
import com.abc.domain.Member;
import com.abc.domain.Order;
import com.abc.domain.Receipt;
import com.abc.domain.Review;
import com.abc.domain.Store;
import com.abc.domain.Wishlist;
import com.abc.service.DeliveryService;
import com.abc.service.MemberService;
import com.abc.util.FileService;
import com.abc.util.PageNavigator;

import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.proxy.annotation.Post;

@Slf4j
@Controller
@RequestMapping("delivery")
public class DeliveryController {

	String d = "delivery";
	
	@Autowired
	private DeliveryService dService;
	
	@Autowired
	private MemberService mService;
	
	// 첨부파일 업로드 패스 지정
	@Value("${spring.servlet.multipart.location}")		// 설정파일(application 파일)의 속성을 가지고 오고 싶을떄 사용할 수 있는 annotation(spring)
	private String uploadPath;
	
	@Value("${user.board.page}")
	private int countPerPage;
	
	@Value("${user.board.group}")
	private int pagePerGroup;
	
	@GetMapping("/index")
	public String deliveryIndex(Model model,
			@AuthenticationPrincipal UserDetails user) {
		
		Member member = mService.selectOneMember(user.getUsername());
		
		model.addAttribute("member", member);
		
		return d+ "/index"; 
	}
	
	@GetMapping("/read")
	public String deliveryRead(Model model, int num,
			@AuthenticationPrincipal UserDetails user) {
		
		// 0925 추가
		Member member = mService.selectOneMember(user.getUsername());
		model.addAttribute("member", member);
		
		Store store = dService.selectOneStore(num);
		
		log.debug("store : {}", store);
		
		model.addAttribute("store", store);
		return d+ "/read"; 
	}
	

	@GetMapping("/inputStore")
	public String inputStore() {
		return d + "/inputStore";
	}
	
	@PostMapping("/inputStore")
	public String inputStore(Store store,
			@RequestParam MultipartFile file,
			@AuthenticationPrincipal UserDetails user) {
		
		log.debug("input store : {}", store);
		// 첨부된 오리지널 파일의 파일명 출력 
		log.debug("input file : {}", file.getOriginalFilename());
		
		Member member = mService.selectOneMember(user.getUsername());
		
		store.setMemberNum(member.getMemberNum());
		
		log.debug("dd : {}", member.getMemberNum());
		
		
		if(!file.isEmpty()) {
			// 저장할 파일명 생성
			String savedFile = FileService.saveFile(file, uploadPath);
			log.debug("savedFuke : {}", savedFile);
			store.setSavedFile(savedFile);
			store.setOriginalFile(file.getOriginalFilename());
			
		}
		log.debug("store : {} ", store);
		dService.insertStore(store);
		
		return "redirect:./index";
	}
	
	@GetMapping("/storeList")
	public String storeList(Model model, String searchWord,
			@AuthenticationPrincipal UserDetails user) {
		Member member = mService.selectOneMember(user.getUsername());
		model.addAttribute("searchWord",searchWord);
		
		model.addAttribute("member", member);
		return d + "/storeList";
	}
	
	@GetMapping("/storeListAjax")
	public @ResponseBody List<Store> storeListAjax(String title, String category) {

		log.debug("category : {}", category);
		log.debug("title : {}", title);
		List<Store> list = dService.selectSearch(title, category);
		return list;
	}
	
	@GetMapping("/storeDisplay")
	public ResponseEntity<Resource> storeDisplay(int num) {
		
		log.debug("num : {}", num);
		
		Store store = dService.selectOneStore(num);
		
		Resource resource 
			= new FileSystemResource(uploadPath + "/" + store.getSavedFile());
	
		// 파일이 존재하지 않을때
		if(!resource.exists()) {
			return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
		} 
		
		HttpHeaders header = new HttpHeaders();
		
		Path filePath = null;
		
		try {
			filePath = Paths.get(uploadPath + "/" + store.getSavedFile());
			
			// response의 header에
			// 제가 첨부한 내용의 타입은 파일이에요
			header.add("Content-type", Files.probeContentType(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
	}
	
	@GetMapping("/inputItem")
	public String inputItem(Model model,
			@AuthenticationPrincipal UserDetails user) {
		
		Member member = mService.selectOneMember(user.getUsername());
		List<Store> storeList = dService.selectMemberOne(member.getMemberNum());
		
		model.addAttribute("storeList", storeList);
		
		return d + "/inputItem";
	}
	
	@PostMapping("/inputItem")
	public String inputItem(Item item,
			@RequestParam MultipartFile file,
			@AuthenticationPrincipal UserDetails user) {
		log.debug("item : {}", item);
		log.debug("input store : {}", item);
		// 첨부된 오리지널 파일의 파일명 출력 
		log.debug("input file : {}", file.getOriginalFilename());
		
		if(!file.isEmpty()) {
			// 저장할 파일명 생성
			String savedFile = FileService.saveFile(file, uploadPath);
			log.debug("savedFuke : {}", savedFile);
			item.setSavedFile(savedFile);
			item.setOriginalFile(file.getOriginalFilename());
			
		}
		dService.insertItem(item);
		
		return "redirect:./index";
	}
	
	@GetMapping("/itemListAjax")
	public @ResponseBody List<Item> itemListAjax(String title, String storeNum) {
		
		List<Item> list = dService.selectItemSearch(title, storeNum);
		
		return list;
	}
	
	@GetMapping("/itemDisplay")
	public ResponseEntity<Resource> itemDisplay(int num) {
		
		log.debug("num : {}", num);
		
		Item item = dService.selectOneItem(num);
		
		Resource resource 
			= new FileSystemResource(uploadPath + "/" + item.getSavedFile());
	
		// 파일이 존재하지 않을때
		if(!resource.exists()) {
			return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
		} 
		
		HttpHeaders header = new HttpHeaders();
		
		Path filePath = null;
		
		try {
			filePath = Paths.get(uploadPath + "/" + item.getSavedFile());
			
			// response의 header에
			// 제가 첨부한 내용의 타입은 파일이에요
			header.add("Content-type", Files.probeContentType(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
	}
	
	@GetMapping("/reOrderListAjax")
	public @ResponseBody List<Order> reOrderListAjax(int storeNum,
			@AuthenticationPrincipal UserDetails user) {
		
		Member member = mService.selectOneMember(user.getUsername());
		
		List<Order> list = dService.selectMyOrder(member.getMemberNum(), storeNum);
		
		
		return list;
	}
	
	@GetMapping("/checkItem")
	public @ResponseBody Order checkItem(int storeNum, int itemNum,
			@AuthenticationPrincipal UserDetails user) {
		
		Item item = dService.selectOneItem(itemNum);
		
		Member member = mService.selectOneMember(user.getUsername());
		
		List<Order> oList = dService.selectMyOrder(member.getMemberNum(), storeNum);
		
		log.debug("oList의 : {}", oList);
		
		Order order = null;
		
		for (int i = 0; i < oList.size(); i++) {
			log.debug("for문 시작");
			if (oList.get(i).getItemNum() == itemNum) {
				order = oList.get(i);
				log.debug("order체크1 : {}", order);
			}
		}
		log.debug("order체크2 : {}", order);
		return order;
	}
	
	
	@GetMapping("/orderListAjax")
	public @ResponseBody List<Order> orderListAjax(int storeNum, int itemNum,
			@AuthenticationPrincipal UserDetails user) {
		
		log.debug("orderListAjax() 시작");
		
		Order order = new Order();
		
		Member member = mService.selectOneMember(user.getUsername());
		
		List<Order> list = dService.selectMyOrder(member.getMemberNum(), storeNum);
		
		log.debug("list : {}", list);
		log.debug("itemNum은 : {}", itemNum);
		if(itemNum != 0) {
			log.debug("orderListAjax() if 시작");
			
			Item item = dService.selectOneItem(itemNum);
			
			if (list.isEmpty()) {
				log.debug("list.isEmpty() 실행");
				order.setMemberNum(member.getMemberNum());
				order.setItemName(item.getItemName());
				order.setItemPrice(item.getItemPrice());
				order.setItemNum(itemNum);
				order.setStoreNum(storeNum);
				
				dService.insertOrder(order);
			} 
			
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getItemNum() == itemNum && list.get(i).getMemberNum() == member.getMemberNum()) {
					log.debug("list plusOrder() 실행");
					dService.plusOrder(list.get(i));
				} else {
					log.debug("for else set() 실행");
					order.setMemberNum(member.getMemberNum());
					order.setItemName(item.getItemName());
					order.setItemPrice(item.getItemPrice());
					order.setItemNum(itemNum);
					order.setStoreNum(storeNum);
					
					dService.insertOrder(order);
					break;
				}
			}
			list = dService.selectMyOrder(member.getMemberNum(), storeNum);
			return list;
		} else {
			return list;
		}
	}
	
	@GetMapping("plusOrder")
	public @ResponseBody Order plusOrder(int orderNum) {
		
		Order order = dService.selectOneOrder(orderNum);
		
		dService.plusOrder(order);
		
		order = dService.selectOneOrder(orderNum);
		
		return order;
	}
	
	@GetMapping("minusOrder")
	public @ResponseBody Order minusOrder(int orderNum) {
		
		Order order = dService.selectOneOrder(orderNum);
		
		if (order.getQuantity() > 1) {
			dService.minusOrder(order);
		}
		
		order = dService.selectOneOrder(orderNum);
		
		return order;
	}
	
	@GetMapping("/orderPrice")
	public @ResponseBody int orderPrice(int storeNum,
			@AuthenticationPrincipal UserDetails user) {
		
		int result = 0;
		
		Member member = mService.selectOneMember(user.getUsername());
		
		List<Order> oList = dService.selectMyOrder(member.getMemberNum(), storeNum);
		
		for (int i = 0; i < oList.size(); i++) {
			log.debug("complete : {}", oList.get(i).getComplete());
			if (oList.get(i).getComplete().equals("N")) {
				result = dService.paymentOrder(member.getMemberNum(), storeNum);
			}
		}
		
		
		return result;
	}
	
	
	@GetMapping("/deleteOrder")
	public @ResponseBody int deleteOrder(int orderNum) {
		
		int result = dService.deleteOrder(orderNum);
		
		return result;
	}
	

	
	
	@GetMapping("/myCompleteOrderList")	
	public String myCompleteOrderList(Model model,
			@AuthenticationPrincipal UserDetails user) {
		
		Member member = mService.selectOneMember(user.getUsername());
		
		List<Receipt> rList = dService.selectReceipt(member.getMemberNum());
		//Review review = dService.selectReviewsByMemberNum(member.getMemberNum());
		model.addAttribute("rList", rList);
		log.debug("rList : {}", rList);
		
		/*
		 * int num = 1;
		 * 
		 * for (int i = 0; i < rList.size(); i++) { if(review == null) { num = 1; } else
		 * if (rList.get(i).getReceiptNum() == review.getReceiptNum()) { num = 0; } }
		 * model.addAttribute("num", num);
		 */
		return d + "/myCompleteOrderList";
	}
	
	@PostMapping("/myCompleteOrderList")
	public String myCompleteOrderList(Review review,
			@AuthenticationPrincipal UserDetails user) {
		
		Member member = mService.selectOneMember(user.getUsername());
		Receipt receipt = dService.selectReceiptByReceiptNum(review.getReceiptNum());
		Store store = dService.selectOneStore(review.getStoreNum());
		
		review.setOrderHistory(receipt.getOrderHistory());
		review.setMemberNum(member.getMemberNum());
		review.setNickname(member.getNickname());
		
		log.debug("review : {}", review);
		
		dService.insertReview(review);
		
		List<Review> rList = dService.selectReviews(review.getStoreNum());

		log.debug("rList : {}", rList);
		int sum = 0;
		for (int i = 0; i < rList.size(); i++) {
			sum += rList.get(i).getRating();  
		}
		log.debug("sum : {}", sum);
		double rate = sum / rList.size();
		
		store.setRating(rate);
		
		dService.updateRating(store);
		
		return "redirect:./index";
	}
	
	@GetMapping("/checkReview")
	public @ResponseBody Review checkReview(int receiptNum) {
		
		Review review = dService.selectReviewsByReceiptNum(receiptNum);
		
		return review;
	}
	
	
	@GetMapping("/allOrder")
	public @ResponseBody Receipt allOrder(int data, int storeNum,
			String address,
			@AuthenticationPrincipal UserDetails user) {
		log.debug("allOrder() 시작");
		Receipt receipt = new Receipt();
		
		Member member = mService.selectOneMember(user.getUsername());
		
		receipt.setMemberNum(member.getMemberNum());
		
		List<Order> oList = dService.selectMyOrders(member.getMemberNum());
		
		String orderItems = "";
		for (int i = 0; i < oList.size(); i++) {
			// 1001 추가
			if (i == oList.size() - 1) {
				orderItems += oList.get(i).getItemName() + " " + oList.get(i).getQuantity() + "개 ";
				dService.completeOrder(oList.get(i).getOrderNum());
			} else {
				orderItems += oList.get(i).getItemName() + " " + oList.get(i).getQuantity() + "개, ";
				dService.completeOrder(oList.get(i).getOrderNum());
			}
		}
		log.debug("orderItems : {}", orderItems);
		
		receipt.setOrderHistory(orderItems);
		receipt.setTotalAmount(data);
		receipt.setStoreNum(storeNum);
		
		// 0923 추가
		receipt.setOrderAddress(address);
		//
		
		int pay = 0;
		log.debug("point : {}", member.getMemberPoint());
		log.debug("data : {}", data);
		if (member.getMemberPoint() > data) {
			pay	= member.getMemberPoint() - data;
			dService.insertReceipt(receipt);
		} else {
			pay = member.getMemberPoint();
			receipt = null;
		}
		
		
		log.debug("pay : {}", pay);
		
		member.setMemberPoint(pay);
		
		mService.useMyPoint(member);
		
		return receipt;
	}
	
	@GetMapping("/printReviewAjax")
	public @ResponseBody List<Review> printReviewAjax(int storeNum) {
		
		List<Review> rList = dService.selectReviews(storeNum);
		
		return rList;
	}
	
	////////////////////////////////////////////////
	// 0923 추가
	@GetMapping("/checkAddress")
	public @ResponseBody Member checkAddress(String loginUser) {
		Member member = mService.selectOneMember(loginUser);
		
		return member;
	}
	
	
	///////////////////////////////////////////////
	
	@GetMapping("/clickWish")
	public @ResponseBody Store clickWish(int storeNum,
			@AuthenticationPrincipal UserDetails user) {
		Member member = mService.selectOneMember(user.getUsername());
		
		Wishlist wish = dService.selectWishlist(storeNum, member.getMemberNum());
		
		if (wish == null) {
			wish = new Wishlist();
			wish.setMemberNum(member.getMemberNum());
			wish.setStoreNum(storeNum);
			wish.setMemberId(user.getUsername());
			
			dService.insertWishlist(wish);
			dService.plusWishlist(storeNum);
		} else {
			dService.deleteWish(storeNum, member.getMemberNum());
			dService.minusWishlist(storeNum);
		}
		Store store = dService.selectOneStore(storeNum);
		return store;
	}
	
	
	@GetMapping("/loadWish")
	public @ResponseBody Wishlist loadWish(int storeNum,
			@AuthenticationPrincipal UserDetails user) {
		Member member = mService.selectOneMember(user.getUsername());
		
		Wishlist wish = dService.selectWishlist(storeNum, member.getMemberNum());
		
		
		return wish;
	}
	
	@GetMapping("/sellerPage")
	public String sellerPage(Model model,
			@AuthenticationPrincipal UserDetails user) {
			Member member = mService.selectOneMember(user.getUsername());
			List<Store> storeList = dService.selectMemberOne(member.getMemberNum());
			
			model.addAttribute("storeList", storeList);
		
		
		return d + "/sellerPage";
	}
	
	@GetMapping("/sellerOrderList")
	public String sellerOrderList() {
		
		return d + "/sellerOrderList";
	}
	
	@GetMapping("/selectMyStore")
	public @ResponseBody List<Store> selectMyStore(
			@AuthenticationPrincipal UserDetails user) {
		
		Member member = mService.selectOneMember(user.getUsername());
		List<Store> storeList = dService.selectMemberOne(member.getMemberNum());
		
		return storeList;
	}
	
	@GetMapping("/loadSellerOrderList")
	public @ResponseBody List<Receipt> loadSellerOrderList(int storeNum) {
		
		List<Receipt> rList = dService.selectReceiptByStoreNum(storeNum);
		
		log.debug("판매자 주문 목록 리스트 : {}", rList);
		
		return rList;
	}
	
	@GetMapping("/storeRank")
	public @ResponseBody List<Store> storeRank() {
		
		List<Store> sList = dService.selectStoreRank();
		log.debug("sList : {}", sList);
		return sList;
	}
	
	
	@GetMapping("/leftoverPoint")
	public @ResponseBody Member leftoverPoint(
			@AuthenticationPrincipal UserDetails user) {
		Member member = mService.selectOneMember(user.getUsername());
		
		return member;
	}
	
	@GetMapping("/checkOrder")
	public @ResponseBody Receipt checkOrder(int receiptNum) {
		
		Receipt receipt = dService.selectReceiptByReceiptNum(receiptNum);
		
		dService.updateReceiptByWaiting(receiptNum);
		
		return receipt;
		
	}
	
	@GetMapping("/startDelivery")
	public @ResponseBody Receipt startDelivery(int receiptNum) {
		Receipt receipt = dService.selectReceiptByReceiptNum(receiptNum);
		log.debug("waiting : {}",receipt.toString());
		if(receipt.getWaiting().equals("Y")) {
			dService.updateReceiptByComplete(receiptNum);
		} else {
			receipt = null;
		}
		return receipt;
	}
	
	
	// 9월 29일 작업
		@GetMapping("/searchResultScroll")
		public String searchResultScroll(Model model,
				@RequestParam(name = "page", defaultValue="1") int page,
				String searchWord) {
			
				PageNavigator navi =  dService.getPageNavigator(
						pagePerGroup, countPerPage, page, searchWord);
			List<Store> sList = null;
				
			sList = dService.selectAllStore(navi, searchWord);
			navi =  dService.getPageNavigator(
				pagePerGroup, countPerPage, page, searchWord);
			
			model.addAttribute("sList",sList);
			model.addAttribute("navi",navi);
			
			return d + "/searchResultScroll";
		}
		
		@GetMapping("/searchResultScrollAjax")
		public @ResponseBody List<Store> searchStore( String searchWord,
				@RequestParam(name = "page", defaultValue="1") int page) {

				List<Store> sList = null;
				PageNavigator navi =  dService.getPageNavigator(
						pagePerGroup, countPerPage, page, searchWord);
				
				sList = dService.selectAllStore(navi, searchWord);
				
				return sList;
		}
		
		@GetMapping("/storeListPaging")
		public @ResponseBody List<Store> storeListPaging ( String searchWord,
				@RequestParam(name = "page", defaultValue="1") int page) {
			
			log.debug("여기까지 굿");
			PageNavigator navi =  dService.getPageNavigator(
					pagePerGroup, countPerPage, page, searchWord);
			
			log.debug("여기까지 완료");
			List<Store> slist = dService.selectAllStore(navi, searchWord);
			
			return slist;
		}
	
	
	
	
}
