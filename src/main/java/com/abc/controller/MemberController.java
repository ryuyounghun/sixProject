package com.abc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.abc.domain.Member;
import com.abc.service.MemberService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping("member")
public class MemberController {

	@Autowired
	private MemberService mService;
	
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
	
	@GetMapping("/login")
	public String login() {
		return "memberView/login";
	}
	
	
	// ID 중복체크 창을 띄우는 기능
		@GetMapping("/idCheck")
		public String idCheck() {
			log.debug("idCheck() 실행");
			return "memberView/idChkForm";
		}
		
		// ID 중복체크를 실행하는 기능
		@PostMapping("/idCheck")
		public String idCheck(String searchId, Model model) {
			log.debug("searchId : {}", searchId);
			
			// DB에서 ID를 조회한 결과
			Member searchedMember = mService.selectOneMember(searchId);
			
			log.debug("검색 결과 : {}", searchedMember);
			
			// 검색 결과가 없으면 true, 있으면 false
			boolean  result = searchedMember == null ? true : false;
			
			log.debug("id 사용가능여부 : {}", result);
			
			model.addAttribute("searchId", searchId);
			model.addAttribute("result", result);
			
			return "memberView/idChkForm";
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
}
