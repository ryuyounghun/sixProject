package com.abc.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.abc.domain.ClassBoard;
import com.abc.domain.FreeBoard;
import com.abc.domain.Member;
import com.abc.service.ClassBoardService;
import com.abc.service.FreeBoardService;
import com.abc.service.MemberService;
import com.abc.util.FileService;
import com.abc.util.PageNavigator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("community")
public class ComunnityController{

	String c = "community/";

	@Autowired
	ClassBoardService cService; 
	
	@Autowired
	FreeBoardService fService; 
	
	@Autowired
	MemberService mService; 
	
	// page당 글 수
	@Value("${user.board.page}")
	private int countPerPage;

	// 업로드 패스
	@Value("${spring.servlet.multipart.location}") // 설정파일(properties)의 속성의 값을 가지고 오고 싶을 때 사용할 수 있는 annotation
	private String uploadPath;
	
	// 그룹 당 page 수
	@Value("${user.board.group}")
	private int pagePerGroup;
	
	@GetMapping({"/","/index"})
	public String communityIndex(Model model,
			@RequestParam(name="page", defaultValue = "1" )int page) {
		log.debug("index실행");
		
		List<ClassBoard> cbList = null;
		PageNavigator navi = cService.getPageNavigator(
				pagePerGroup, countPerPage, page); 
		
		cbList = cService.selectAllClassBoard(navi);
		
		log.debug("cbList의 길이 : {}",cbList.size());
		
		model.addAttribute(navi);
		model.addAttribute("cbList",cbList);
		
		return c + "index"; 
	}
	
	@GetMapping("/read")
	public String communityRead() {
		log.debug("읽기파일");
		return c  + "read"; 
	}
	
	@GetMapping("/classWrite")
	public String classWrite() {
		log.debug("classWrite() 실행");
		return c + "classWrite";
	}

	@PostMapping("/classWrite")
	public String classWrite(ClassBoard cBoard,
			@AuthenticationPrincipal UserDetails user) {

		log.debug("Post classWrite()시작");

		Member member = mService.selectOneMember(user.getUsername());	

		String mNickname = member.getNickname();
		cBoard.setNickname(mNickname);
		
		int mNum = Integer.parseInt(member.getMemberNum());
		cBoard.setMemberNum(mNum);
		
		log.debug("write cBoard : {}", cBoard);
		cService.insertClassBoard(cBoard);
		
		return "redirect:./index"; // .../board/
	}
	
	@GetMapping("/freeWrite")
	public String freeWrite() {
		log.debug("freeWrite() 실행");
		return c + "freeWrite";
	}
	
	@PostMapping("/freeWrite")
	public String freeWrite(FreeBoard fBoard,
			@AuthenticationPrincipal UserDetails user,
			Model model,
			@RequestParam MultipartFile[] files) {

		log.debug("Post freeWrite()시작");
		log.debug("FreeBoard : {}", fBoard);
	
		try {
			boolean isRegistered = fService.registerBoard(fBoard, files);
		} catch  ( Exception e) {
			e.printStackTrace();
			
		}
		
		return "redirect:./index"; // .../board/
				
		
		
		/*
		Member member = mService.selectOneMember(user.getUsername());	
		
		String mNickname = member.getNickname();
		cBoard.setNickname(mNickname);
		
		int mNum = Integer.parseInt(member.getMemberNum());
		cBoard.setMemberNum(mNum);
		
		log.debug("write cBoard : {}", cBoard);
		cService.insertClassBoard(cBoard);
		*/
	}
	
	@GetMapping("/selectDestination")
	public String selectDestination(RedirectAttributes re) {
		log.debug("목적지 정하기");
		
		return c + "selectDestination";
	}
	
	
	
}
