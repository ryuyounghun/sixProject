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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.abc.domain.Member;
import com.abc.domain.MyCoupon;
import com.abc.service.MasterService;
import com.abc.service.MemberService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping("member")
public class MemberController {

	@Autowired
	private MemberService mService;

	@Autowired
	private MasterService mtService;
	
	@GetMapping("/join")
	public String join() {
		log.debug("join() 실행");
		return "memberView/join";
	}
	
	// 회원가입을 실행하는 기능
	@PostMapping("/join")
	public String join(Member member) {
		log.debug("member의 정보 : {}", member);
		
		// service에 Member 객체 전송
		int result = mService.insertMember(member);
		
		return "redirect:/";
		
	}
	// ID 중복체크(ajax)
	@PostMapping("/checkId")
	@ResponseBody
	public int checkId(@RequestParam("id") String id) {
		int result = mService.checkId(id);
		return result;
	}
	
	@GetMapping("/login")
	public String login() {
		return "memberView/login";
	}
	
	
		@PostMapping("/socialIdCheck")
		public @ResponseBody Member kakaoIdCheck(String id) {
			
			Member member = mService.selectOneMember(id);
			log.debug("member:{}", member);
			return member;
		}
		
		@PostMapping("/kakaoSignUp")
		public @ResponseBody Member kakaoSignUp(String userid, String name, String email) {
			
			log.debug("signUp 시작");
			
			Member member = new Member();
			
			member.setMemberId(email);
			member.setMemberPw(userid);
			member.setMemberName(name);
			member.setNickname(name);
			member.setAddress("다시 입력해 주세요");
			member.setPhone("다시 입력해 주세요");
			
			mService.insertMember(member);
			
			member = mService.selectOneMember(email);
			
			log.debug("member:{}", member);
			return member;
		}
		
		@PostMapping("/naverSignUp")
		public @ResponseBody Member naverSignUp(String userid, String name,
				String email, String nickname, String phone) {
			
			log.debug("signUp 시작");
			
			Member member = new Member();
			
			member.setMemberId(email);
			member.setMemberPw(userid);
			member.setMemberName(name);
			member.setNickname(name);
			member.setAddress("다시 입력해 주세요");
			member.setPhone(phone);
			
			mService.insertMember(member);
			
			member = mService.selectOneMember(email);
			
			log.debug("member:{}", member);
			return member;
		}
		
		@GetMapping("/mypage")
		public String mypage(Model model,
				@AuthenticationPrincipal UserDetails user) {
			
			Member member = mService.selectOneMember(user.getUsername());
			
			model.addAttribute("member", member);
			
			return "memberView/mypage";
		}
		
		@GetMapping("/myCoupons")
		public String myCoupons() {
			
			return "memberView/myCoupons";
		}
		
		@GetMapping("/myCouponList")
		public @ResponseBody List<MyCoupon> myCouponList(
				@AuthenticationPrincipal UserDetails user) {
			Member member = mService.selectOneMember(user.getUsername());
			
			List<MyCoupon> myCList = mtService.selectAllMyCoupon(member.getMemberNum());
			
			log.debug("myCList : {}", myCList);
			
			
			return myCList;
		}
		
		@GetMapping("/useCoupon")
		public @ResponseBody String useCoupon(int myCouponNum,
				@AuthenticationPrincipal UserDetails user) {
			Member member = mService.selectOneMember(user.getUsername());
			MyCoupon myCoupon = mtService.useOneMyCoupon(myCouponNum);
			
			member.setMemberPoint(myCoupon.getCouponPoint());
			
			mService.updatePoint(member);
			
			mtService.deleteOneMyCoupon(myCouponNum);
			
			return "ㅇㅇ";
		}
		
		@GetMapping("/myPoint")
		public @ResponseBody Member myPoint(
				@AuthenticationPrincipal UserDetails user) {
			
			Member member = mService.selectOneMember(user.getUsername());
			
			return member;
			
		}
		
		
		// 회원정보 수정 화면 띄우기
		@GetMapping("/updateInfo")
		public String updateInfo(@AuthenticationPrincipal UserDetails user, Model model) {
			
			// 사용자 식별자
			String memberId = user.getUsername();
			
			log.debug("memberId : {}", memberId);
			
			Member member = mService.selectOneMember(memberId);
			
			log.debug("member : {}", member);
		
			
			model.addAttribute("member", member);
			
			return "memberView/updateInfo";
		}
		
		// 회원정보 수정
		@PostMapping("/updateInfo")
		public String update(Member member, @AuthenticationPrincipal UserDetails user) { // 지금 로그인 되어있는 객체를 가져오는 @AuthenticationPrincipal
		
			log.debug("member : {}" , member);
			log.debug("username : {}", user.getUsername());
			member.setMemberId(user.getUsername()); 
			int result = mService.updateMember(member);
			log.debug("result : {}", result);
			return "redirect:/";
		}
		
		
		
}
