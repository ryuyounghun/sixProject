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
import com.abc.domain.ClassRoom;
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
public class ComunnityController {

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

	@GetMapping({ "/", "/index" })
	public String communityIndex(Model model, @RequestParam(name = "page", defaultValue = "1") int page) {
		log.debug("index실행");

		List<ClassBoard> cbList = null;
		List<FreeBoard> fbList = null;
		PageNavigator cbNavi = cService.getPageNavigator(pagePerGroup, countPerPage, page);

		PageNavigator fbNavi = fService.getPageNavigator(pagePerGroup, countPerPage, page);
		fbList = fService.selectAllFreeBoard(fbNavi);
		cbList = cService.selectAllClassBoard(cbNavi);
		// fbList = fService.selectAllClassBoard (navi);
		log.debug("cbList의 길이 : {}", cbList.size());
		log.debug("fbList의 길이 : {}", fbList.size());

		model.addAttribute(cbNavi);
		model.addAttribute("fbList", fbList);
		model.addAttribute("cbList", cbList);

		return c + "index";
	}

	@GetMapping("/read")
	public String communityRead(Model model, int num, @AuthenticationPrincipal UserDetails user) {
		log.debug("읽기파일");

		log.debug("num : {}", num);

		ClassBoard cBoard = cService.selectOneClassBoard(num);
		Member member = mService.selectOneMember(user.getUsername());
		model.addAttribute("cBoard", cBoard);
		model.addAttribute("member", member);

		return c + "read";
	}

	@GetMapping("/classWrite")
	public String classWrite() {
		log.debug("classWrite() 실행");
		return c + "classWrite";
	}

	@PostMapping("/classWrite")
	public String classWrite(ClassBoard cBoard, @AuthenticationPrincipal UserDetails user) {

		log.debug("Post classWrite()시작");
		ClassRoom cRoom = new ClassRoom();
		Member member = mService.selectOneMember(user.getUsername());

		String mNickname = member.getNickname();
		cBoard.setNickname(mNickname);

		int mNum = member.getMemberNum();
		cBoard.setMemberNum(mNum);

		log.debug("write cBoard : {}", cBoard);
		cService.insertClassBoard(cBoard);

		cRoom.setClassNum(cBoard.getClassNum());
		cRoom.setMemberNum(member.getMemberNum());
		cRoom.setAddress(member.getAddress());
		cRoom.setNickname(mNickname);

		log.debug("cRoom : {}", cRoom);

		cService.insertClassRoom(cRoom);

		return "redirect:./index"; // .../board/
	}

	@GetMapping("/freeWrite")
	public String freeWrite() {
		log.debug("freeWrite() 실행");
		return c + "freeWrite";
	}

	@PostMapping("/freeWrite")
	public String freeWrite(FreeBoard fBoard, @AuthenticationPrincipal UserDetails user, Model model,
			@RequestParam MultipartFile[] files) {

		log.debug("Post freeWrite()시작");
		log.debug("files길이 : {}", files.length);
		log.debug("file정보 : {}", files[0].toString());

		Member m = mService.selectOneMember(user.getUsername());
		fBoard.setMemberNum(m.getMemberNum());
		fBoard.setNickname(m.getNickname());
		log.debug("FreeBoard : {}", fBoard);

		/* 여기까지 실행 완료 */

		if (files.length > 0) {
			try {
				boolean isRegistered = fService.registerBoard(fBoard, files); // 파일 있을 때의 등록
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("등록실패");

			}
		} else { // 파일 없을 때의 등록
			fService.registerBoard(fBoard);
		}

		return "redirect:./index"; // .../board/

		/*
		 * Member member = mService.selectOneMember(user.getUsername());
		 * 
		 * String mNickname = member.getNickname(); cBoard.setNickname(mNickname);
		 * 
		 * int mNum = Integer.parseInt(member.getMemberNum());
		 * cBoard.setMemberNum(mNum);
		 * 
		 * log.debug("write cBoard : {}", cBoard); cService.insertClassBoard(cBoard);
		 */
	}

	@GetMapping("/selectDestination")
	public String selectDestination(RedirectAttributes re) {
		log.debug("목적지 정하기");

		return c + "selectDestination";
	}

	@GetMapping("/partyIndex")
	public String partyIndex() {

		return c + "partyIndex";
	}

	@GetMapping("/setPartBox")
	public @ResponseBody List<ClassBoard> setPartBox() {

		List<ClassBoard> cList = cService.selectAllClassBoardNoParameter();

		return cList;
	}

	@GetMapping("/partyPeople")
	public @ResponseBody List<ClassRoom> partyPeople(int classNum) {
		
		List<ClassRoom> cRList = cService.selectClassRoom(classNum);
		
		return cRList;
		
	}
	
	@GetMapping("/joinParty")
	public @ResponseBody String joinParty(int classNum,
			@AuthenticationPrincipal UserDetails user) {
		Member member = mService.selectOneMember(user.getUsername());
		ClassBoard cBoard = cService.selectOneClassBoard(classNum);
		ClassRoom cRoom = cService.selectClassRoomByMemberNumAndClassNum(member.getMemberNum(), classNum);
		log.debug("cRoom : {}", cRoom);
		int totalMember = cBoard.getTotalMember();
		log.debug("totalMember : {}", totalMember);
		List<ClassRoom> cRList = cService.selectClassRoom(classNum);
		log.debug("cRList : {}", cRList.size());
		
		String anwser = "이미 정원입니다.";
		
		if (totalMember > cRList.size()) {
			anwser = "이미 파티원입니다.";
			if (cRoom == null) {
				cRoom = new ClassRoom();
				cRoom.setAddress(member.getAddress());
				cRoom.setClassNum(classNum);
				cRoom.setMemberNum(member.getMemberNum());
				cRoom.setNickname(member.getNickname());
				
				cService.insertClassRoom(cRoom);
				anwser = "추가되었습니다.";
			}
		}
		return anwser;
	}
	
	@GetMapping("/withdrawalParty")
	public @ResponseBody void withdrawalParty(int classNum,
			@AuthenticationPrincipal UserDetails user) {
		Member member = mService.selectOneMember(user.getUsername());
		cService.withdrawalParty(member.getMemberNum(), classNum);
	}
}
