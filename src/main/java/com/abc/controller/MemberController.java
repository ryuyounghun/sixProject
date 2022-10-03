package com.abc.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.abc.domain.ChatRoom;
import com.abc.domain.Coupon;
import com.abc.domain.ExpBoard;
import com.abc.domain.GuestBook;
import com.abc.domain.Member;
import com.abc.domain.MyCoupon;
import com.abc.domain.Receipt;
import com.abc.domain.Review;
import com.abc.domain.Store;
import com.abc.domain.Wishlist;
import com.abc.service.ChatService;
import com.abc.service.DeliveryService;
import com.abc.service.ExpBoardService;
import com.abc.service.MasterService;
import com.abc.service.MemberService;
import com.abc.util.FileService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping("member")
public class MemberController {

	@Autowired
	private MemberService mService;

	@Autowired
	private MasterService mtService;
	
	// 0923 추가
	@Autowired
	private ChatService chatService;
	
	@Autowired
	private DeliveryService dService;
	
	@Autowired
	private ExpBoardService expService;
	
	// 첨부파일 업로드 패스 지정 0924 추가
	@Value("${spring.servlet.multipart.location}")		// 설정파일(application 파일)의 속성을 가지고 오고 싶을떄 사용할 수 있는 annotation(spring)
	private String uploadPath;
	
	// 레벨 계산 메서드
	public int calcLevel(int exp) {
		List<ExpBoard> list = expService.getLevelList();
		int mExp = exp;
		int mLevel = 1;
		log.debug("mExp: {}", mExp);
		log.debug("list toString : {}", list.toString());
		for ( int i = 0; i< list.size(); i++) {
			if ( i == list.size()-1){
				log.debug("레벨2 : {}", i);
				log.debug("레벨2 : {}", list.get(i).getUserLevel());
				log.debug("레벨2 : {}", list.get(i).getUserExp());
				mLevel = list.get(i).getUserLevel();
				
			}
			else if ( list.get(i).getUserExp() <= mExp
					 && list.get(i+1).getUserExp() > mExp ) {
				
				log.debug("레벨1 : {}",list.get(i).getUserExp());
				log.debug("레벨1 : {}",list.get(i).getUserLevel());
				mLevel = list.get(i).getUserLevel();
				break;
			}
		}
		log.debug("레벨5 : {}", mLevel);
		return mLevel;
	}

	
	
	
	@GetMapping("/join")
	public String join() {
		log.debug("join() 실행");
		return "memberView/join";
	}
	
	// 회원가입을 실행하는 기능
	@PostMapping("/join")
	public String join(Member member) {
		log.debug("member의 정보 : {}", member);
		
		
		if (member.getNickname().isEmpty()) {
			member.setNickname(member.getMemberName());
		}
		
		int result = mService.insertMember(member);
		// 채팅방 생성 0923 추가
		ChatRoom ctRoom = chatService.createRoom(member.getMemberName());
		
    	// 채팅방 생성 끝
    	// 0924 추가
		member.setRoomId(ctRoom.getRoomId());
		mService.setRoomId(member);
    	///////////
    	// service에 Member 객체 전송 
    	
		return "redirect:/";
		
	}
	// ID 중복체크(ajax)
	@PostMapping("/checkId")
	@ResponseBody
	public int checkId(@RequestParam("id") String id) {
		int result = mService.checkId(id);
		return result;
	}
	
	// 0925 세련씨
	// pinNumber 중복체크(ajax)
	@PostMapping("/checkPnum")
	@ResponseBody
	public int checkPnum(@RequestParam("pnum") String pnum) {
		int result = mService.checkPnum(pnum);
		return result;
	}
	
	@GetMapping("/findIdPw")
	public String findIdPw(){
		
		return "memberView/findIdPw";
	}	
	
	@PostMapping("/findMemberId")
	@ResponseBody
	public String findMemberId(String pinNumber) {
		log.debug("pinNumber:{}", pinNumber );
		
		String result = mService.findMemberId(pinNumber);
		return result;
	}
		

	
	//Post
	@PostMapping("/updatePw")
	@ResponseBody
	public int updatePw(String pw, String pnum) {
		
		Member member = new Member();
		//핀번호에 맞는 mdId 찾고
		String memberId = mService.findMemberId(pnum);
		member = mService.selectOneMember(memberId);
		member.setMemberPw(pw);
		
		//set 비밀번호 위에 파라미터에 담긴거 저장하기 그 다음 업데이트해주기
		int result = mService.updatePw(member);
		//비밀번호 암호화 잊지말기(-> serviceImpl에서 해줌)
		
		return result;
	}
	
	
	// 세련씨
	
	
	
	@GetMapping("/login")
	public String login() {
		return "memberView/login";
	}
	
	
	@PostMapping("/socialIdCheck")
	public @ResponseBody Member kakaoIdCheck(String id) {
		
		Member member = mService.selectOneMember(id);
		log.debug("member:{}", member);
		return member;
	}
	
	@PostMapping("/kakaoSignUp")
	public @ResponseBody Member kakaoSignUp(String userid, String name, String email) {
		
		log.debug("signUp 시작");
		
		
		Member member = new Member();
		
		member.setMemberId(email);
		member.setMemberPw(userid);
		member.setMemberName(name);
		member.setNickname(name);
		member.setAddress("다시 입력해 주세요");
		member.setPhone("다시 입력해 주세요");
		
		mService.insertMember(member);
		
		// 채팅방 생성 0923 추가
		ChatRoom ctRoom = chatService.createRoom(name);
		
    	// 채팅방 생성 끝
    	
		// 0924 추가
		member.setRoomId(ctRoom.getRoomId());
		mService.setRoomId(member);
    	///////////
				
		
		member = mService.selectOneMember(email);
		
		log.debug("member:{}", member);
		return member;
	}
	
	@PostMapping("/naverSignUp")
	public @ResponseBody Member naverSignUp(String userid, String name,
			String email, String nickname, String phone) {
		
		log.debug("signUp 시작");
		
		Member member = new Member();
		
		member.setMemberId(email);
		member.setMemberPw(userid);
		member.setMemberName(name);
		member.setNickname(name);
		member.setAddress("다시 입력해 주세요");
		member.setPhone(phone);
		mService.insertMember(member);
		
		// 채팅방 생성 0923 추가
		ChatRoom ctRoom = chatService.createRoom(name);
		
    	// 채팅방 생성 끝
		
		// 0924 추가
		member.setRoomId(ctRoom.getRoomId());
		mService.setRoomId(member);
    	///////////

		
		member = mService.selectOneMember(email);
		
		log.debug("member:{}", member);
		return member;
	}
		
	@GetMapping("/mypage")
	public String mypage(Model model, int num,
			@AuthenticationPrincipal UserDetails user) {
		
		Member member = mService.selectOneMember(user.getUsername());
		Member memberPage = mService.selectOneMemberByMemberNum(num);
		// 0924 추가
		List<Coupon> cList = mtService.selectAllCoupon();
		model.addAttribute("cList", cList);
		///////////
		
		int level = calcLevel(member.getMemberExp());
		member.setMemberLevel(level);
		log.debug("level : {}", member.getMemberLevel());
		
		model.addAttribute("member", member);
		model.addAttribute("memberPage", memberPage);
		
		
		return "memberView/mypage";
	}
	
	@GetMapping("/myCoupons")
	public String myCoupons() {
		
		return "memberView/myCoupons";
	}
	
	@GetMapping("/myCouponList")
	public @ResponseBody List<MyCoupon> myCouponList(
			@AuthenticationPrincipal UserDetails user) {
		Member member = mService.selectOneMember(user.getUsername());
		
		List<MyCoupon> myCList = mtService.selectAllMyCoupon(member.getMemberNum());
		
		log.debug("myCList : {}", myCList);
		
		
		return myCList;
	}
	
	@GetMapping("/useCoupon")
	public @ResponseBody List<MyCoupon> useCoupon(int myCouponNum,
			@AuthenticationPrincipal UserDetails user) {
		Member member = mService.selectOneMember(user.getUsername());
		MyCoupon myCoupon = mtService.useOneMyCoupon(myCouponNum);
		
		member.setMemberPoint(myCoupon.getCouponPoint());
		
		mService.updatePoint(member);
		
		mtService.deleteOneMyCoupon(myCouponNum);
		
		List<MyCoupon> myCList = mtService.selectAllMyCoupon(member.getMemberNum());
		
		return myCList;
	}
	
	@GetMapping("/myPoint")
	public @ResponseBody Member myPoint(int memberNum,
			@AuthenticationPrincipal UserDetails user) {
		
		Member member = mService.selectOneMemberByMemberNum(memberNum);
		
		return member;
		
	}
	
	
	// 회원정보 수정 화면 띄우기
	@GetMapping("/updateInfo")
	public String updateInfo(@AuthenticationPrincipal UserDetails user, Model model) {
		
		// 사용자 식별자
		String memberId = user.getUsername();
		
		log.debug("memberId : {}", memberId);
		
		Member member = mService.selectOneMember(memberId);
		
		log.debug("member : {}", member);
	
		
		model.addAttribute("member", member);
		
		return "memberView/updateInfo";
	}
	
	// 회원정보 수정
	@PostMapping("/updateInfo")
	public String update(Member member, @AuthenticationPrincipal UserDetails user) { // 지금 로그인 되어있는 객체를 가져오는 @AuthenticationPrincipal
	
		log.debug("member : {}" , member);
		log.debug("username : {}", user.getUsername());
		member.setMemberId(user.getUsername()); 
		int result = mService.updateMember(member);
		log.debug("result : {}", result);
		return "redirect:/";
	}
	
	@PostMapping("/writeReply")
	@ResponseBody
	public void writeReply(String content, int memberNum, @AuthenticationPrincipal UserDetails user) {
	
		log.debug("content : {}", content);
		log.debug("memberNum : {}", memberNum);
		
		GuestBook guestbook = new GuestBook();
		
		Member member = mService.selectOneMember(user.getUsername());
		
		guestbook.setNickname(member.getNickname());
		guestbook.setContent(content);
		guestbook.setWriterNum(member.getMemberNum());
		guestbook.setMemberNum(memberNum);
		
		mService.insertMyPage(guestbook);
		
	}	
	
	@GetMapping("/loadAllReply") //writeId = memberId 
	public @ResponseBody List<GuestBook> loadAllReply(int memberNum){
		
		
		log.debug("마이페이지 주인 멤버넘 : {}", memberNum);
		
		List<GuestBook> gList = mService.selectAllReply(memberNum);
		
		log.debug("gList : {}", gList.size());
		
		return gList;
	}
	
	// 0924 추가
	@GetMapping("/lookWishList")
	@ResponseBody 
	public List<Store> lookWishList(int memberNum) {
		List<Wishlist> wList = dService.selectWishlistByMemberNum(memberNum);
		Store store;
		List<Store> sList = new ArrayList<Store>();
		
		for (int i = 0; i < wList.size(); i++) {
			store = dService.selectOneStore(wList.get(i).getStoreNum());
			sList.add(store);
		}
		log.debug("sList : {}", sList);
		return sList;
	}
	
	// 0924 추가
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
	
	
	// 0925 세련씨
	
	@GetMapping("/blockLogin")//Model은 무조건 html로 데이터를 보내주는 상자 
	public String blockLogin(Member member, Model model, @AuthenticationPrincipal UserDetails user) {
		
		log.debug("memberId의 값: {}" , member);
		member = mService.selectOneMember(user.getUsername());
		//"${member}"와 아래 "member" 같은 거임. (이름 무좍권 같게 해야한다!!!!!!!!!!!)
		model.addAttribute("member", member);
		
		return "memberView/blockLogin"; 
	}
	
	@PostMapping("/blockLogin")
	public String blockLogin(@AuthenticationPrincipal UserDetails user) {
		Member member = mService.selectOneMember(user.getUsername());
		mService.blockLogin(member);
		
		return "redirect:/";
	}
	// 0925 세련씨
	
	// 0925 추가
	@GetMapping("/myOrderList")
	public @ResponseBody List<Receipt> myOrderList(int memberNum) {
		
		List<Receipt> rList = dService.selectReceipt(memberNum);
		log.debug("rList : {}", rList);
		return rList;
	}
	
	// 0925 추가
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
		
		return "redirect:/";
	}
	
	// 0925 추가
	@GetMapping("/myReviews")
	@ResponseBody
	public List<Review> myReviews(int memberNum) {
		
		List<Review> rList = dService.selectReviewListByMemberNum(memberNum);
		return rList;
	}
	
	// 0924 추가
		@GetMapping("/memberDisplay")
		public ResponseEntity<Resource> memberDisplay(int num) {
			
			log.debug("num : {}", num);
			
			Member member = mService.selectOneMemberByMemberNum(num);
			Resource resource 
				= new FileSystemResource(uploadPath + "/" + member.getSavedFile());
		
			// 파일이 존재하지 않을때
			if(!resource.exists()) {
				return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
			} 
			
			HttpHeaders header = new HttpHeaders();
			
			Path filePath = null;
			
			try {
				filePath = Paths.get(uploadPath + "/" + member.getSavedFile());
				
				// response의 header에
				// 제가 첨부한 내용의 타입은 파일이에요
				header.add("Content-type", Files.probeContentType(filePath));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
		}
	
		// 0928 추가
		@GetMapping("/mypageImage")
		@ResponseBody 
		public Member mypageImage(int memberNum) {
			
			Member member = mService.selectOneMemberByMemberNum(memberNum);
			
			return member;
		}

		// 0928 추가
		@PostMapping("/updateImage")
		public String updateImage(@RequestParam MultipartFile file,
				Member member) {
			
			if(!file.isEmpty()) {
				// 저장할 파일명 생성
				String savedFile = FileService.saveFile(file, uploadPath);
				log.debug("savedFuke : {}", savedFile);
				member.setSavedFile(savedFile);
				member.setOriginalFile(file.getOriginalFilename());
				
			}
			log.debug("member : {}", member);
			
			mService.updateMyImage(member);
			return "redirect:/";
		}

		// 1001 추가
		@GetMapping("/checkComplete")
		@ResponseBody
		public Receipt checkComplete(int receiptNum) {
			Receipt receipt = dService.selectReceiptByReceiptNum(receiptNum);
			
			return receipt;
		}
		
		// 1001 추가
		@GetMapping("/updateCheckGuestbook")
		@ResponseBody
		public GuestBook updateCheckGuestbook(int guestBookNum) {
			GuestBook gBook = mService.selectGuestBookByNum(guestBookNum);
			
			return gBook;
		}
		
		// 1001 추가
		@GetMapping("/updateGuestbook")
		@ResponseBody
		public void updateGuestbook(int guestBookNum, String content) {
			GuestBook gBook = mService.selectGuestBookByNum(guestBookNum);
			gBook.setContent(content);
			mService.updateGuestBook(gBook);
		}
		
		// 1001 추가
		@GetMapping("/deleteGuestbook")
		@ResponseBody
		public void deleteGuestbook(int guestBookNum) {
			mService.deleteGuestBook(guestBookNum);
		}
		
		//1002 추가
		@PostMapping("/checkPin")
		public @ResponseBody int checkPin(String checkPin,
				@AuthenticationPrincipal UserDetails user) {
			Member member = mService.selectOneMember(user.getUsername());
			int answer = 0;
			log.debug("checkPin1 : {}", checkPin);
			log.debug("checkPin2 : {}", member.getPinNumber());
			if (member.getPinNumber().equals(checkPin)) {
				answer = 1;
			} else {
				answer = 0;
			}
			return answer;
		}
		
		// 1003 추가
		@PostMapping("/mypageLevel")
		@ResponseBody
		public Member mypageLevel(int memberNum) {
			Member member = mService.selectOneMemberByMemberNum(memberNum);
			
			return member;
		}

}
