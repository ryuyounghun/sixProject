package com.abc.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.abc.domain.FileDTO;
import com.abc.domain.FreeBoard;
import com.abc.domain.Member;
import com.abc.domain.Reply;

import com.abc.service.ClassBoardService;
import com.abc.service.FreeBoardService;
import com.abc.service.MemberService;
import com.abc.service.ReplyService;
import com.abc.util.PageNavigator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/community")
public class ComunnityController{

	String c = "community/";

	@Autowired
	ClassBoardService cService; 
	
	
	@Autowired
	FreeBoardService fService; 
	
	@Autowired
	MemberService mService; 
	
	@Autowired
	ReplyService rService; 
	
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
		PageNavigator cbNavi = cService.getPageNavigator(
				pagePerGroup, countPerPage, page); 
		
		List<FreeBoard> fbList = null;
		PageNavigator fbNavi = fService.getPageNavigator(
				pagePerGroup, countPerPage, page); 
		fbList = fService.selectAllFreeBoard(fbNavi);
		cbList = cService.selectAllClassBoard(cbNavi);
		// fbList = fService.selectAllClassBoard (navi);
		log.debug("cbList의 길이 : {}",cbList.size());
		log.debug("fbList의 길이 : {}",fbList.size());
		
		
		model.addAttribute(cbNavi);
		model.addAttribute("fbList",fbList);
		model.addAttribute("cbList",cbList);
		
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
		
		return c  + "read"; 
	}
	// 채팅방 
    
	@GetMapping("/classWrite")
	public String classWrite() {
		log.debug("classWrite() 실행");
		return c + "classWrite";
	}

	@PostMapping("/classWrite")
	public String classWrite(ClassBoard cBoard,
			@AuthenticationPrincipal UserDetails user) {

		log.debug("Post classWrite()시작");
		ClassRoom cRoom = new ClassRoom();
		Member member = mService.selectOneMember(user.getUsername());	

		// 게시판 객체 설정
		String mNickname = member.getNickname();
		cBoard.setNickname(mNickname);
		
		int mNum = member.getMemberNum();
		cBoard.setMemberNum(mNum);
		
		log.debug("write cBoard : {}", cBoard);
		cService.insertClassBoard(cBoard);
		
		// 파티원객체 생성
		cRoom.setClassNum(cBoard.getClassNum());
		cRoom.setMemberNum(member.getMemberNum());
		cRoom.setAddress(member.getAddress());
		cRoom.setNickname(mNickname);

		log.debug("cRoom : {}", cRoom);

		cService.insertClassRoom(cRoom);
		return "redirect:./index"; // .../board/
	}
	
	@GetMapping("/freeIndex")
	public String freeIndex(Model model,
			@RequestParam(name="page", defaultValue = "1" )int page) {
		
		List<FreeBoard> fbList = null;
		PageNavigator fbNavi = fService.getPageNavigator(
				pagePerGroup, countPerPage, page); 
		fbList = fService.selectAllFreeBoard(fbNavi);

		model.addAttribute("fbList",fbList);
		return c + "freeIndex";
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
		log.debug("files길이 : {}",files.length);
		// 제대로 들어갔나 한번더 확인
		log.debug("file정보 : {}",files.toString());
		
		
		Member m = mService.selectOneMember(user.getUsername());
		fBoard.setMemberNum(m.getMemberNum());
		fBoard.setNickname(m.getNickname());
		log.debug("FreeBoard : {}", fBoard);
		
		if ( files.length > 0 ) {
			try {
				boolean isRegistered = fService.registerBoard(fBoard, files); //파일 있을 때의 등록
			} catch  ( Exception e) {
				e.printStackTrace();
				System.out.println("등록실패");
				
			}
		}
		else { //파일 없을 때의 등록
			fService.registerBoard(fBoard);
		}
		
		return "redirect:./index"; // .../board/
				
	}
	@GetMapping("/fbRead")
	public String fbRead(int num, Model model) {
		log.debug("fbread()");
		log.debug("fbread() num : {} ", num);
	
		
		//보드정보 호출
		FreeBoard freeBoard = fService.selectOneFreeBoard(num);
		// 보드에 해당하는 파일 호출
		List<FileDTO> fileList = fService.selectFileList(num);
		
		// 해당 글 번호의 댓글 정보 가져오기
		model.addAttribute("board", freeBoard);
		model.addAttribute("fileList", fileList);
		log.debug("파일리스트투스트링: {}", fileList.toString());
		return c + "freeRead";
	}
	
	@GetMapping("/selectDestination")
	public String selectDestination(RedirectAttributes re) {
		log.debug("목적지 정하기");
		
		return c + "selectDestination";
	}

	@GetMapping("/display")
	public ResponseEntity<Resource> display(long num) {
	  
		log.debug("num : {}", num);
		FileDTO file = fService.selectOneFile(num);
		log.debug("저장된 파일 :{}", file.getSaveName());
		Resource resource = 
				new FileSystemResource(uploadPath + "/" + file.getSaveName());
		
		// 파일이 존재하지 않을 때
		if ( !resource.exists()) {
			// 404 요소 없음
			 return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
			// return new ResponseEntity<Resource>(HttpStatus.OK);
			
		}
		HttpHeaders header = new HttpHeaders();
		
		// 경로를 가져오기 
		Path filePath = null;
		
		try {
			// 파일 경로
			// 첨부한 내용의 타입은 파일
			filePath = Paths.get(uploadPath + "/" + file.getSaveName());
			header.add("Content-type", Files.probeContentType(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
	}

	@PostMapping("/fbWriteReply")
	public String fbWriteReply(Reply reply, @AuthenticationPrincipal UserDetails user) {
		log.debug("reply : {}", reply);
		
		String memberId = user.getUsername();
		Member member = mService.selectOneMember(memberId);
		
		reply.setMemberNum(member.getMemberNum());
		
		rService.insertReply(reply);
		
		// 하나의 메소드를 최대한 활용하기위ㅐ 쿼리스트링으로 보내주기
		// #(id값) : 해당 id로 포커스()
		return "redirect:./fbRead?num="+reply.getBoardNum() + "#reList";
	}
	
	@GetMapping("/getAllfbReply")
	public @ResponseBody List<Reply> getAllfbReply(int num) {
		log.debug("글번호 :  {} ", num);
		
		List<Reply> replyList = rService.selectReplyByBoardNum(num);
		
		log.debug("replyList :  {} ", replyList.size());
		// 호출한곳으로 보내기
		return replyList;
	}
	@GetMapping("/getOneReply")
	public @ResponseBody Reply getOneReply(int replyNum) {
		log.debug("댓글번호 :  {} ", replyNum);
		
		Reply reply = rService.selectOneReply(replyNum);
		
		log.debug("reply :  {} ", reply);
		// 호출한곳으로 보내기
		return reply;
	}

	@PostMapping("/updateReply")
	public @ResponseBody String updateReply(Reply reply) {
		log.debug("reply수정 : {}", reply);
		
		int result = rService.updateReply(reply);
		
		if ( result != 0 ) {
			return "changeSuccess"; 
		}else {
			return "changeFailed";
		}
	}
	
	@GetMapping("/deleteReply")
	public String deleteReply(int num) {
		log.debug("num : {}", num);
		
		Reply reply = rService.selectOneReply(num);
		log.debug("reply : {}", reply);
		rService.deleteReply(num);
		
		return "redirect:./fbRead?num="+reply.getBoardNum();
	}
	
	
	@GetMapping("/partyIndex")
	public String partyIndex(Model model,
			@RequestParam(name = "page", defaultValue="1") int page,
			String searchWord) {
		
		PageNavigator navi = cService.getPageNavigator(
				pagePerGroup, 
				countPerPage,
				page,
				searchWord);
		
		model.addAttribute("navi",navi);
		return c + "partyIndex";
	}

	@GetMapping("/setPartBox")
	public @ResponseBody List<ClassBoard> setPartBox(
			String searchWord,
			@RequestParam(name = "page", defaultValue="1") int page
			,Model model) {

		
		log.debug(searchWord);
		List<ClassBoard> cbList = null;
		PageNavigator navi = cService.getPageNavigator(
				pagePerGroup, 
				countPerPage,
				page,
				searchWord);
		log.debug("페이지 : {}", navi.getCurrentPage());
		log.debug("페이지 : {}", navi.getEndPageGroup());
		
		// ------------------------------
		if ( searchWord == null || searchWord.trim().length() == 0 ) {
			
			// 나중에 10개씩 출력하는걸로 바꾸기
			// cbList =  cService.selectAllClassBoardNoParameter();

			cbList = cService.selectAllClassBoard(navi);
		
		}else {
			
			
			cbList = cService.selectAllClassBoard(navi, searchWord);
		}
		return cbList;
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
