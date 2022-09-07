package com.abc.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.abc.domain.ClassBoard;
import com.abc.util.FileService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("community")
public class ComunnityController {

	String c = "community/";
	
	@GetMapping({"/","/index"})
	public String communityIndex() {
		
		return c + "index"; 
	}
	
	@GetMapping("/read")
	public String communityRead() {
		log.debug("읽기파일");
		return c  + "read"; 
	}
	
	@GetMapping("/classWrite")
	public String write() {
		log.debug("write() 실행");
		return c + "classWrite";
	}

	@PostMapping("/classWrite")
	public String write(ClassBoard cBoard, String rdTag) {

		log.debug("Post write()시작");

		log.debug("write cBoard : {}", cBoard);
		log.debug("rdTag : {}", rdTag);
		
		
		return "redirect:./index"; // .../board/
	}
	
	@GetMapping("/selectDestination")
	public String selectDestination(RedirectAttributes re) {
		log.debug("목적지 정하기");
		
		return c + "selectDestination";
	}
}
