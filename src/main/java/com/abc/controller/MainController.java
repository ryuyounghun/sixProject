package com.abc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.abc.domain.Member;
import com.abc.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MainController {

	@Autowired
	private MemberService mService;
	
	// 1004 추가
	@GetMapping("/")
	public String index(Model model,
			@AuthenticationPrincipal UserDetails user) {
		log.debug("index실행 ");
		if (user != null) {
			Member member = mService.selectOneMember(user.getUsername());
			model.addAttribute("member", member);
		}
		
		return "/index";
	}
	
	@PostMapping("/addressAndPhone")
	public @ResponseBody Member addressAndPhone(String address, String phone,
			@AuthenticationPrincipal UserDetails user) {
		log.debug("user : {}", user);
		Member member = mService.selectOneMember(user.getUsername());
		
		if (phone == null) {
			
			member.setAddress(address);
		} else {
			member.setAddress(address);
			member.setPhone(phone);
		}
		
		mService.updateAddressAndPhone(member);
		return member;
	}
	
	@GetMapping("/memberCheck")
	public @ResponseBody Member memberCheck(@AuthenticationPrincipal UserDetails user) {
		
		Member member = mService.selectOneMember(user.getUsername());
		
		return member;
	}
	
	@PostMapping("/checkPhone")
	   public @ResponseBody Member phoneCheck(String phone) {
	      Member member = mService.selectOneMember(phone);
	      return member;
	   }
}
