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

import com.abc.domain.ChatRoom;
import com.abc.domain.ClassBoard;
import com.abc.domain.ClassRoom;
import com.abc.domain.ExpBoard;
import com.abc.domain.FileDTO;
import com.abc.domain.FreeBoard;
import com.abc.domain.Member;
import com.abc.domain.Reply;
import com.abc.domain.Store;
import com.abc.service.ChatService;
import com.abc.service.ClassBoardService;
import com.abc.service.DeliveryService;
import com.abc.service.ExpBoardService;
import com.abc.service.FreeBoardService;
import com.abc.service.MemberService;
import com.abc.service.ReplyService;
import com.abc.util.FileService;
import com.abc.util.PageNavigator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.proxy.annotation.Post;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/community")
public class ComunnityController{

	String c = "community/";

	@Autowired
	private ClassBoardService cService; 
	
	@Autowired
	private ChatService chatService;
	
	@Autowired
	private FreeBoardService fService; 
	
	@Autowired
	private MemberService mService; 
	
	@Autowired
	private ReplyService rService; 
	
	@Autowired
	private ExpBoardService expService;
	
	@Autowired
	private DeliveryService dService; 
	
	
	// page??? ??? ???
	@Value("${user.board.page}")
	private int countPerPage;

	// 1004 ?????? page??? ??? ??? classWrite
	private int classCountPerPage = 2;
	
	// ????????? ??????
	@Value("${spring.servlet.multipart.location}") // ????????????(properties)??? ????????? ?????? ????????? ?????? ?????? ??? ????????? ??? ?????? annotation
	private String uploadPath;
	
	// ?????? ??? page ???
	@Value("${user.board.group}")
	private int pagePerGroup;
	
	
	
	// ??? ??? ??? ????????? ?????? ???????????????
	private void addExp(String memberId) {
			
		mService.addExp(memberId);
	}
	
	// ?????? ??? ??? ????????? ?????? ???????????????
	private void addExpByReply(String memberId) {
		
		mService.addExpByReply(memberId);
		
	}
	// ?????? ?????? ?????????
	public int calcLevel(int exp) {
		List<ExpBoard> list = expService.getLevelList();
		int mExp = exp;
		int mLevel = 1;
		log.debug("mExp: {}", mExp);
		log.debug("list toString : {}", list.toString());
		for ( int i = 0; i< list.size(); i++) {
			if ( i == list.size()-1){
				log.debug("??????2 : {}", i);
				log.debug("??????2 : {}", list.get(i).getUserLevel());
				log.debug("??????2 : {}", list.get(i).getUserExp());
				mLevel = list.get(i).getUserLevel();
				
			} else if(0 <= mExp && list.get(0).getUserExp() > mExp) {
				mLevel = 1;
				break;
			}
			else if ( list.get(i).getUserExp() <= mExp
					 && list.get(i+1).getUserExp() > mExp ) {
				
				log.debug("??????1 : {}",list.get(i).getUserExp());
				log.debug("??????1 : {}",list.get(i).getUserLevel());
				mLevel = list.get(i).getUserLevel();
				break;
			}
		}
		log.debug("??????5 : {}", mLevel);
		return mLevel;
	}

	
	
	@GetMapping({"/","/index"})
	public String communityIndex(Model model,
			@RequestParam(name="page", defaultValue = "1" )int page,
			// 0924 ??????
			@AuthenticationPrincipal UserDetails user) {
		log.debug("index??????");
		
		// 0924 ??????
		Member member = mService.selectOneMember(user.getUsername());
		model.addAttribute("member", member);
		//
		
		List<ClassBoard> cbList = null;
		List<FreeBoard> fbList = null;
		PageNavigator cbNavi = cService.getPageNavigator(
				pagePerGroup, countPerPage, page); 
		
		PageNavigator fbNavi = fService.getPageNavigator(
				pagePerGroup, countPerPage, page); 
		fbList = fService.selectAllFreeBoard(fbNavi, null);
		cbList = cService.selectAllClassBoard(cbNavi);
		// fbList = fService.selectAllClassBoard (navi);
		log.debug("cbList??? ?????? : {}",cbList.size());
		log.debug("fbList??? ?????? : {}",fbList.size());
		
		model.addAttribute(cbNavi);
		model.addAttribute("fbList",fbList);
		model.addAttribute("cbList",cbList);
		
		return c + "index"; 
	}
	
	// 1004 ??????
	@GetMapping("/read")
	public String communityRead(Model model, int num, @AuthenticationPrincipal UserDetails user) {
		log.debug("????????????");
		
		log.debug("num : {}", num);
		// ????????? ??????
		cService.updateViewCount(num);
		//
		ClassBoard cBoard = cService.selectOneClassBoard(num);
		Member member = mService.selectOneMember(user.getUsername());
		model.addAttribute("cBoard", cBoard);
		model.addAttribute("member", member);
		log.debug("cBoard id : {}", cBoard.getRoomId());
		return c  + "read"; 
	}
    
	// 1004 ??????
	@GetMapping("/classWrite")
	public String classWrite(Model model,
			@AuthenticationPrincipal UserDetails user) {
		log.debug("classWrite() ??????");
		Member member = mService.selectOneMember(user.getUsername());
		model.addAttribute("member", member);
		return c + "classWrite";
	}

	// ????????? ??????
	@PostMapping("/classWrite")
	public String classWrite(ClassBoard cBoard,
			@RequestParam MultipartFile file,
			@AuthenticationPrincipal UserDetails user) {

		log.debug("Post classWrite()??????");
		ClassRoom cRoom = new ClassRoom();
		Member member = mService.selectOneMember(user.getUsername());	

		
		// ?????? ?????? ??????
		addExp(user.getUsername());
		member.setMemberLevel( calcLevel(member.getMemberExp()) );
		cBoard.setMemberLevel(member.getMemberLevel());
		cRoom.setMemberLevel(member.getMemberLevel());
		
		// ????????? ?????? ??????
		String mNickname = member.getNickname();
		cBoard.setNickname(mNickname);
		
		int mNum = member.getMemberNum();
		cBoard.setMemberNum(mNum);
		
		
		log.debug("write cBoard : {}", cBoard);
		
		// ????????? ??????
		ChatRoom ctRoom = chatService.createRoom(cBoard.getTitle());
		
    	// ????????? ?????? ???
    	
		if(!file.isEmpty()) {
			// ????????? ????????? ??????
			String savedFile = FileService.saveFile(file, uploadPath);
			log.debug("savedFuke : {}", savedFile);
			cBoard.setSavedFile(savedFile);
			cBoard.setOriginalFile(file.getOriginalFilename());
		}
		
    	// ?????? ??????
    	cBoard.setRoomId(ctRoom.getRoomId());
    	cService.insertClassBoard(cBoard);
		// ???????????? ??????
    	
    	// ??????????????? ??????
		cRoom.setClassNum(cBoard.getClassNum());
		cRoom.setMemberNum(member.getMemberNum());
		cRoom.setAddress(member.getAddress());
		cRoom.setNickname(mNickname);
		// 1006 ??????
		cRoom.setOriginalFile(member.getOriginalFile());
		cRoom.setSavedFile(member.getSavedFile());
		

		log.debug("cRoom : {}", cRoom);

		cService.insertClassRoom(cRoom);
		
		return "redirect:./index"; // .../board/
	}
	
	// 1002 ?????? ??????
	@GetMapping("/freeIndex")
	public String freeIndex(Model model, String searchWord,
			@AuthenticationPrincipal UserDetails user,
			@RequestParam(name="page", defaultValue = "1" )int page) {
		Member member = mService.selectOneMember(user.getUsername());
		List<FreeBoard> fbList = null;
		PageNavigator navi = fService.getPageNavigator(
				pagePerGroup, countPerPage, page, searchWord); 
		fbList = fService.selectAllFreeBoard(navi, searchWord);
		
		model.addAttribute("searchWord", searchWord);
		model.addAttribute("member", member);
		model.addAttribute("navi" , navi);
		model.addAttribute("fbList",fbList);
		return c + "freeIndex";
	}
	
	@GetMapping("/freeWrite")
	public String freeWrite(@AuthenticationPrincipal UserDetails user,
			Model model) {
		log.debug("freeWrite() ??????");
		Member member = mService.selectOneMember(user.getUsername());
		
		model.addAttribute("member", member);
		
		return c + "freeWrite";
	}
	
	@PostMapping("/freeWrite")
	public String freeWrite(FreeBoard fBoard,
			@AuthenticationPrincipal UserDetails user,
			Model model,
			@RequestParam MultipartFile[] files) {

		log.debug("Post freeWrite()??????");
		log.debug("files?????? : {}",files.length);
		// ????????? ???????????? ????????? ??????
		log.debug("file?????? : {}",files.toString());
		
		
		Member m = mService.selectOneMember(user.getUsername());
		fBoard.setMemberNum(m.getMemberNum());
		fBoard.setNickname(m.getNickname());
		fBoard.setMemberLevel(m.getMemberLevel());
		log.debug("FreeBoard : {}", fBoard);
		
		
		// ?????? ?????? ??????
		addExp(user.getUsername());
		m.setMemberLevel( calcLevel(m.getMemberExp()) );
		fBoard.setMemberLevel(m.getMemberLevel());
		
		
		if ( files.length > 0 ) {
			try {
				boolean isRegistered = fService.registerBoard(fBoard, files); //?????? ?????? ?????? ??????
			} catch  ( Exception e) {
				e.printStackTrace();
				System.out.println("????????????");
				
			}
		}
		else { //?????? ?????? ?????? ??????
			fService.registerBoard(fBoard);
		}
		
		return "redirect:./freeIndex"; // .../board/
	}
	
	@GetMapping("/fbRead")
	public String fbRead(int num, Model model,
			// 0924 ??????
			@AuthenticationPrincipal UserDetails user) {
		log.debug("fbread()");
		log.debug("fbread() num : {} ", num);
		// 0924 ??????
		Member member = mService.selectOneMember(user.getUsername());
		model.addAttribute("member", member);
		////////////
		
		// 0930 ??????
		fService.updateViewCount(num);
		///////////
		
		//???????????? ??????
		FreeBoard freeBoard = fService.selectOneFreeBoard(num);
		// ????????? ???????????? ?????? ??????
		List<FileDTO> fileList = fService.selectFileList(num);
		
		// ?????? ??? ????????? ?????? ?????? ????????????
		model.addAttribute("board", freeBoard);
		model.addAttribute("fileList", fileList);
		log.debug("???????????????????????????: {}", fileList.toString());
		return c + "freeRead";
	}
	
	@GetMapping("/selectDestination")
	public String selectDestination(RedirectAttributes re) {
		log.debug("????????? ?????????");
		
		return c + "selectDestination";
	}

	@GetMapping("/display")
	public ResponseEntity<Resource> display(long num) {
	  
		log.debug("num : {}", num);
		FileDTO file = fService.selectOneFile(num);
		log.debug("????????? ?????? :{}", file.getSaveName());
		Resource resource = 
				new FileSystemResource(uploadPath + "/" + file.getSaveName());
		
		// ????????? ???????????? ?????? ???
		if ( !resource.exists()) {
			// 404 ?????? ??????
			 return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
			// return new ResponseEntity<Resource>(HttpStatus.OK);
		}
		HttpHeaders header = new HttpHeaders();
		
		// ????????? ???????????? 
		Path filePath = null;
		try {
			// ?????? ??????
			// ????????? ????????? ????????? ??????
			filePath = Paths.get(uploadPath + "/" + file.getSaveName());
			header.add("Content-type", Files.probeContentType(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
	}

	@PostMapping("/fbWriteReply")
	public String fbWriteReply(Reply reply,
			@AuthenticationPrincipal UserDetails user) {
		log.debug("reply : {}", reply);
		
		String memberId = user.getUsername();
		Member member = mService.selectOneMember(memberId);
		
		reply.setMemberNum(member.getMemberNum());
		
		// ?????? ?????? ??????
		addExpByReply(user.getUsername());
		member.setMemberLevel( calcLevel(member.getMemberExp()) );
		reply.setMemberLevel(member.getMemberLevel());
		
		rService.insertReply(reply);
		
		// ????????? ???????????? ????????? ?????????????????? ????????????????????? ????????????
		// #(id???) : ?????? id??? ?????????()
		return "redirect:./fbRead?num="+reply.getBoardNum() + "#reList";
	}
	
	@GetMapping("/getAllfbReply")
	public @ResponseBody List<Reply> getAllfbReply(int num) {
		log.debug("????????? :  {} ", num);
		
		List<Reply> replyList = rService.selectReplyByBoardNum(num);
		
		log.debug("replyList :  {} ", replyList.size());
		// ?????????????????? ?????????
		return replyList;
	}
	@GetMapping("/getOneReply")
	public @ResponseBody Reply getOneReply(int replyNum) {
		log.debug("???????????? :  {} ", replyNum);
		
		Reply reply = rService.selectOneReply(replyNum);
		
		log.debug("reply :  {} ", reply);
		// ?????????????????? ?????????
		return reply;
	}

	@PostMapping("/updateReply")
	public @ResponseBody String updateReply(Reply reply) {
		log.debug("reply?????? : {}", reply);
		
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
			String searchWord,
			@AuthenticationPrincipal UserDetails user) {
		
		Member member = mService.selectOneMember(user.getUsername());
		
		model.addAttribute("member", member);
		
		log.debug(searchWord);
		List<ClassBoard> cbList = null;
		PageNavigator navi = cService.getPageNavigator(
				pagePerGroup, // 1004 ??????
				classCountPerPage,
				page,
				searchWord);
		log.debug("????????? : {}", navi.getCurrentPage());
		log.debug("????????? : {}", navi.getEndPageGroup());
		
		// ------------------------------
		if ( searchWord == null || searchWord.trim().length() == 0 ) {
			
			// ????????? 10?????? ?????????????????? ?????????
			// cbList =  cService.selectAllClassBoardNoParameter();

			cbList = cService.selectAllClassBoard(navi);
		
		}else {
			
			
			cbList = cService.selectAllClassBoard(navi, searchWord);
		}
		
		model.addAttribute("cbList" , cbList);
		model.addAttribute("navi" , navi);
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
		log.debug("????????? : {}", navi.getCurrentPage());
		log.debug("????????? : {}", navi.getEndPageGroup());
		// ------------------------------
		if ( searchWord == null || searchWord.trim().length() == 0 ) {
			// ????????? 10?????? ?????????????????? ?????????
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
		ClassRoom checkRoom = cService.selectClassRoomByMemberNum(member.getMemberNum());
		
		////////////////////////////////////////// ??????
		String answer = "?????? ???????????????.";
		if (checkRoom == null) {
			if (totalMember > cRList.size()) {
				answer = "?????? ??????????????????.";
				if (cRoom == null) {
					cRoom = new ClassRoom();
					cRoom.setAddress(member.getAddress());
					cRoom.setClassNum(classNum);
					cRoom.setMemberNum(member.getMemberNum());
					cRoom.setNickname(member.getNickname());
					cRoom.setOriginalFile(member.getOriginalFile());
					cRoom.setSavedFile(member.getSavedFile());
					
					cService.insertClassRoom(cRoom);
					answer = "?????????????????????.";
				}
			}
		} else {
			answer = "?????? ????????? ????????? ????????????.";
		}
		return answer;
	}
	
	@GetMapping("/withdrawalParty")
	public @ResponseBody void withdrawalParty(int classNum,
			@AuthenticationPrincipal UserDetails user) {
		Member member = mService.selectOneMember(user.getUsername());
		cService.withdrawalParty(member.getMemberNum(), classNum);
		
		// 0924 ??????
		List<ClassRoom> cRoom = cService.selectClassRoom(classNum);
		log.debug("cRoom : {}", cRoom);
		if(cRoom.isEmpty()) {
			cService.deleteClassBoard(classNum);
		}
		///////////
		
	}
	// 0923??????
	@GetMapping("/realTimeParty")
	public @ResponseBody List<ClassRoom> realTimeParty(
			@AuthenticationPrincipal UserDetails user) {
		
		Member member = mService.selectOneMember(user.getUsername());
		
		ClassRoom classRoom = cService.selectClassRoomByMemberNum(member.getMemberNum());
	
		List<ClassRoom> cList = cService.selectClassRoom(classRoom.getClassNum());
		
		return cList;
	}
	
	// 0923??????
	@GetMapping("/fullParty")
	public @ResponseBody ClassBoard fullParty(
			@AuthenticationPrincipal UserDetails user) {
		
		Member member = mService.selectOneMember(user.getUsername());
		
		ClassRoom classRoom = cService.selectClassRoomByMemberNum(member.getMemberNum());
		
		ClassBoard cBoard = cService.selectOneClassBoard(classRoom.getClassNum());
		
		return cBoard;
	}
	
	// 0923 ??????
	@PostMapping("/deleteClassBoard")
	public @ResponseBody Member deleteClassBoard(int classNum) {
		
		ClassBoard cBoard = cService.selectOneClassBoard(classNum);
		
		Member member = mService.selectOneMemberByMemberNum(cBoard.getMemberNum());
		
		return member;
	}

	// 0923 ??????
	@PostMapping("/deleteBoard")
	@ResponseBody
	public void deleteBoard(int classNum, String roomId) {
		
		cService.deleteClassBoard(classNum);
		chatService.deleteOneRoom(roomId);
		
		
	}
	
	// 0923 ??????
	@GetMapping("/checkJoinedParty")
	public @ResponseBody ClassRoom checkJoinedParty(String memberId) {
		Member member = mService.selectOneMember(memberId);
		
		ClassRoom cRoom = cService.selectClassRoomByMemberNum(member.getMemberNum());
		
		return cRoom;
	}
	
	// 0923 ??????
	@GetMapping("/findStore")
	public @ResponseBody List<Store> findStore(String category) {
		
		List<Store> sList = dService.selectStoreListByCategory(category);
		
		return sList;
	}
	
	// 0924 ??????
	@GetMapping("/setStore")
	public @ResponseBody Store setStore(int storeNum) {
		
		Store store = dService.selectOneStore(storeNum);
		
		return store;
	}
	
	// 0925 ??????
	@GetMapping("/storeInfo")
	@ResponseBody
	public Store storeInfo(int classNum) {
		
		ClassBoard cBoard = cService.selectOneClassBoard(classNum);
		
		Store store = dService.selectStoreByStoreName(cBoard.getTitle());
		
		return store;
	}
	
	// 0925 ??????
	@GetMapping("/storeDisplay")
	public ResponseEntity<Resource> storeDisplay(int num) {
		
		log.debug("num : {}", num);
		
		Store store = dService.selectOneStore(num);
		
		Resource resource 
			= new FileSystemResource(uploadPath + "/" + store.getSavedFile());
	
		// ????????? ???????????? ?????????
		if(!resource.exists()) {
			return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
		} 
		
		HttpHeaders header = new HttpHeaders();
		
		Path filePath = null;
		
		try {
			filePath = Paths.get(uploadPath + "/" + store.getSavedFile());
			
			// response??? header???
			// ?????? ????????? ????????? ????????? ???????????????
			header.add("Content-type", Files.probeContentType(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
	}
	
	// 0929 ?????? ??????????????? 1004 ??????
	@GetMapping("/updateFreeWrite")
	public String updateFreeWrite(int boardNum, Model model,
			@AuthenticationPrincipal UserDetails user) {
		FreeBoard fBoard = fService.selectOneFreeBoard(boardNum);
		log.debug("fBoard : {}", fBoard);
		model.addAttribute("fBoard",fBoard);
		
		List<FileDTO> fileList = fService.selectFileList(boardNum);
		log.debug("fileList : {}", fileList);
		
		model.addAttribute("fileList", fileList);
		
		//
		Member member = mService.selectOneMember(user.getUsername());
		model.addAttribute("member", member);
		//
		
		return c + "updateFreeWrite";
	}
	
	// 0929 ?????? ??????????????? ??????
	@PostMapping("/updateFreeWrite")
	public String updateFreeWrite(FreeBoard fBoard,
			@AuthenticationPrincipal UserDetails user,
			@RequestParam MultipartFile[] files) {
		log.debug("fBoard : {}", fBoard);
		List<FileDTO> fileList = fService.selectFileList(fBoard.getBoardNum());
		int num = fBoard.getBoardNum();
		log.debug("num : {}", num);
		if (files.length > 0) {
			try {
				boolean isRegistered = fService.registerBoard(num, files); //?????? ?????? ?????? ??????
				fService.updateFreeBoard(fBoard);
			} catch  ( Exception e) {
				e.printStackTrace();
				System.out.println("????????????");
				
			}
		}
		else { //?????? ?????? ?????? ??????
			fService.updateFreeBoard(fBoard);
		}
		
		return "redirect:./index";
	}
	
	// 0929 ??????
	@GetMapping("/viewUpdateBtn")
	@ResponseBody
	public FreeBoard viewUpdateBtn(int fBoardNum) {
		FreeBoard fBoard = fService.selectOneFreeBoard(fBoardNum);
		
		return fBoard;
	}
	
	// 0930 ??????
	@GetMapping("/realtimeboard")
	@ResponseBody
	public List<FreeBoard> realtimeboard() {
		List<FreeBoard> fList = fService.selectFreeBoardRank();
		
		return fList;
	}
	
	// 1003 ??????
	@GetMapping("/partyDisplay")
	public ResponseEntity<Resource> partyDisplay(int num) {
		log.debug("num : {}", num);
		
		ClassBoard cBoard = cService.selectOneClassBoard(num);
		
		Resource resource 
			= new FileSystemResource(uploadPath + "/" + cBoard.getSavedFile());
	
		// ????????? ???????????? ?????????
		if(!resource.exists()) {
			return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
		} 
		
		HttpHeaders header = new HttpHeaders();
		
		Path filePath = null;
		
		try {
			filePath = Paths.get(uploadPath + "/" + cBoard.getSavedFile());
			
			// response??? header???
			// ?????? ????????? ????????? ????????? ???????????????
			header.add("Content-type", Files.probeContentType(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
	}

	
	// 1004 ??????
	@GetMapping("/realtimeClassboard")
	@ResponseBody
	public List<ClassBoard> realtimeClassboard() {
		List<ClassBoard> cList = cService.selectClassBoardRank();
		return cList;
	}
	
	// 1006 ??????
	@GetMapping("/memberDisplay")
	public ResponseEntity<Resource> memberDisplay(int num) {
		
		log.debug("num : {}", num);
		
		Member member = mService.selectOneMemberByMemberNum(num);
		Resource resource 
			= new FileSystemResource(uploadPath + "/" + member.getSavedFile());
	
		// ????????? ???????????? ?????????
		if(!resource.exists()) {
			return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
		} 
		
		HttpHeaders header = new HttpHeaders();
		
		Path filePath = null;
		
		try {
			filePath = Paths.get(uploadPath + "/" + member.getSavedFile());
			
			// response??? header???
			// ?????? ????????? ????????? ????????? ???????????????
			header.add("Content-type", Files.probeContentType(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
	}
	
	// 1009 ?????? ????????? ?????? ??????
	@GetMapping("/deleteFreeBoard")
	@ResponseBody
	public void deleteFreeBoard(int fBoardNum) {
		fService.deleteFreeBoard(fBoardNum);
	}
}
