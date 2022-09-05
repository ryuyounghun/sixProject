package com.abc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.abc.domain.Store;
import com.abc.service.DeliveryService;
import com.abc.util.FileService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("store")
public class StoreController {

	String s = "store";
	
	@Autowired
	private DeliveryService sService;
	
	
	// 첨부파일 업로드 패스 지정
	@Value("${spring.servlet.multipart.location}")		// 설정파일(application 파일)의 속성을 가지고 오고 싶을떄 사용할 수 있는 annotation(spring)
	private String uploadPath;
	
	@GetMapping("/inputStore")
	public String inputStore() {
		return s+ "/" + "inputStore";
	}
	
	@PostMapping("/inputStore")
	public String inputStore(Store store,
			@RequestParam MultipartFile file,
			@AuthenticationPrincipal UserDetails user) {
		
		log.debug("input store : {}", store);
		// 첨부된 오리지널 파일의 파일명 출력 
		log.debug("input file : {}", file.getOriginalFilename());
		
		store.setMemberNum(user.getUsername());
		
		if(!file.isEmpty()) {
			// 저장할 파일명 생성
			String savedFile = FileService.saveFile(file, uploadPath);
			log.debug("savedFuke : {}", savedFile);
			store.setSavedFile(savedFile);
			store.setOriginalFile(file.getOriginalFilename());
			
		}
		log.debug("store : {} ", store);
		sService.insertStore(store);
		
		return "redirect:./";
	}
}
