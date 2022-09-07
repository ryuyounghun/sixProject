package com.abc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.abc.domain.Member;
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
	public String deliveryRead() {
		
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
}
