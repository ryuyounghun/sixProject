package com.abc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.abc.domain.Coupon;
import com.abc.domain.Member;
import com.abc.domain.MyCoupon;
import com.abc.service.MasterService;
import com.abc.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("master")
public class MasterController {

	String m = "master/";
	
	@Autowired
	private MasterService mtService;
	
	@Autowired
	private MemberService mService;
	
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
	
	@GetMapping("buyOneCoupon")
	public @ResponseBody String buyOneCoupon(int couponNum,
			@AuthenticationPrincipal UserDetails user) {
		
		Member member = mService.selectOneMember(user.getUsername());
		Coupon coupon = mtService.selectCoupon(couponNum);
		MyCoupon myCoupon = new MyCoupon();
		
		
		
		myCoupon.setCouponNum(couponNum);
		myCoupon.setCouponName(coupon.getCouponName());
		myCoupon.setCouponPoint(coupon.getCouponPoint());
		myCoupon.setMemberNum(member.getMemberNum());
		
		mtService.insertMyCoupon(myCoupon);
		
		return "akak";
	}
}
