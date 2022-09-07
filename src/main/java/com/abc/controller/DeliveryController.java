package com.abc.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

import com.abc.domain.Item;
import com.abc.domain.Member;
import com.abc.domain.Order;
import com.abc.domain.Store;
import com.abc.service.DeliveryService;
import com.abc.service.MemberService;
import com.abc.util.FileService;

import lombok.extern.slf4j.Slf4j;

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
	
	
	@GetMapping("/index")
	public String deliveryIndex() {
		
		return d+ "/index"; 
	}
	
	@GetMapping("/read")
	public String deliveryRead(Model model, int num) {
		
		Store store = dService.selectOne(num);
		
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
	public String storeList() {
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
		
		Store store = dService.selectOne(num);
		
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
	
	@GetMapping("/orderListAjax")
	public @ResponseBody List<Order> orderListAjax(int storeNum, int itemNum,
			@AuthenticationPrincipal UserDetails user) {
		
		Item item = dService.selectOneItem(itemNum);
		Order order = new Order();
		
		Member member = mService.selectOneMember(user.getUsername());
		
		order.setMemberNum(member.getMemberNum());
		order.setItemName(item.getItemName());
		order.setItemPrice(item.getItemPrice());
		order.setItemNum(itemNum);
		order.setStoreNum(storeNum);
		
		dService.insertOrder(order);
		
		
		List<Order> list = dService.selectOrder(order);
		
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getItemNum() == itemNum && list.get(i).getMemberNum() == member.getMemberNum()) {
				
			}
		}
		
		return list;
	}
	
}
