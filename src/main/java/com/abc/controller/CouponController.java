package com.abc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.abc.service.CouponService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/coupon")
public class CouponController {

	@Autowired
	CouponService cpService;
	
	@GetMapping("insertCoupon")
	public String insertCoupon() {
		log.debug("insertCoupon 실행");
		
		return "coupon/insertCoupon";
	}
	
	@GetMapping("buyCoupon")
	public String buyCoupon() {
		log.debug("buyCoupon 실행");
		
		return "coupon/buyCoupon";
		
	}
}
