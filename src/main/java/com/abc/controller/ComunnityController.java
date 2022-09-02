package com.abc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("community")
public class ComunnityController {

	String c = "community";
	
	@GetMapping("/index")
	public String communityIndex() {
		
		return c+ "/" + c + "Index"; 
	}
	
	@GetMapping("/read")
	public String communityRead() {
		log.debug("읽기파일");
		return c + "/" + c + "Read"; 
	}
	
	
}
