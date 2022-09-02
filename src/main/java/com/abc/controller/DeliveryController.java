package com.abc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("delivery")
public class DeliveryController {

	String d = "delivery";
	
	@GetMapping("/index")
	public String deliveryIndex() {
		
		return d+ "/" + d + "index"; 
	}
	
	@GetMapping("/read")
	public String deliveryRead() {
		
		return d+ "/" + d + "read"; 
	}
	
	
}
