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

import com.abc.domain.BannedMember;
import com.abc.domain.Coupon;
import com.abc.domain.ExpBoard;
import com.abc.domain.Member;
import com.abc.domain.MessageReport;
import com.abc.domain.MyCoupon;
import com.abc.service.ChatMessageService;
import com.abc.service.ExpBoardService;
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
	private ChatMessageService cmService;
	
	@Autowired
	private MemberService mService;
	
	@Autowired
	private ExpBoardService expService;
	
	@GetMapping({ "/", "/index" })
	public String index(Model model) {
		
		// 0928 추가 리폿
		List<MessageReport> cmList = cmService.selectAllReportMessage();
		log.debug("cmList : {}", cmList.get(0).toString());
		model.addAttribute("cmList",cmList);
		// 리폿 th
		
		// ban List 0928 추가
		List<Member> bannedList = mtService.selectAllBannedList();
		model.addAttribute("bannedList",bannedList);
		// ban List
		
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
	
	@GetMapping("/reportHandling")
	public String reportHandling(Model model){
		
		List<MessageReport> cmList = cmService.selectAllReportMessage();
		log.debug("cmList : {}", cmList);
		model.addAttribute("cmList",cmList);
		return m+"/reportHandling";
	}
	@GetMapping("/handleReport")
	public String handleReport(int reportNum) {
		log.debug("{}",reportNum);
		MessageReport mr = mtService.selectOneReport(reportNum);
		log.debug("mr : {}", mr.getMessage());
		log.debug("mr : {}", mr.getSender());
		
		mtService.banMember(mr.getSender());
		BannedMember bm = new BannedMember();
		
		bm.setMemberId(mr.getSender());
		bm.setMessage(mr.getMessage());
		bm.setReporter(mr.getReporter());
		
		mtService.insertBanListMember(bm);
		return "redirect:/master/index";
	}
	
	@GetMapping("/bannedList")
	public String bannedList(Model model){
		
		List<Member> bannedList = mtService.selectAllBannedList();
		
		
		model.addAttribute("bannedList",bannedList);
		
		return m+"/bannedList";
	}
	@GetMapping("/unbanMember")
	public String unbanMember(String memberId) {
		
		mtService.unBanMember(memberId);
		mtService.deleteBanMember(memberId);
		
		return "redirect:/master/index";
	}
	
	@PostMapping("/insertExp")
	public String insertExp(ExpBoard exp) {
		
		log.debug("insertExpBoard : {}", exp);
		expService.insertExpBoard(exp);
		
		return "redirect:./";
	}
}
