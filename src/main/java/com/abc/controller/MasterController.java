package com.abc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.abc.domain.Coupon;
import com.abc.service.MasterService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("master")
public class MasterController {

	String m = "master/";
	
	@Autowired
	private MasterService mtService;
	
	@GetMapping({ "/", "/index" })
	public String index() {
		return m + "index";
	}
	
	@GetMapping("insertCoupon")
	public String insertCoupon() {
		log.debug("insertCoupon 실행");
		
		return m + "insertCoupon";
	}
	
	@PostMapping("insertCoupon")
	public String insertCoupon(Coupon coupon) {
		log.debug("insertCoupon post 실행");
		
		mtService.insertCoupon(coupon);
		
		return "redirect:./";
	}

	
	@GetMapping("buyCoupon")
	public String buyCoupon(Model model) {
		log.debug("buyCoupon 실행");
		
		List<Coupon> cList = mtService.selectAllCoupon();
		
		model.addAttribute("cList", cList);
		
		return m + "buyCoupon";
		
	}
}
