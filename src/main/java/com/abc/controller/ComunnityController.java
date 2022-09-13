package com.abc.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.abc.domain.ClassBoard;
import com.abc.domain.FileDTO;
import com.abc.domain.FreeBoard;
import com.abc.domain.Member;
import com.abc.domain.Reply;
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
		List<FreeBoard> fbList = null;
		PageNavigator cbNavi = cService.getPageNavigator(
				pagePerGroup, countPerPage, page); 
		
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
		log.debug("files길이 : {}",files.length);
		log.debug("file정보 : {}",files[0].toString());
		
		
		Member m = mService.selectOneMember(user.getUsername());
		fBoard.setMemberNum(m.getMemberNum());
		fBoard.setNickname(m.getNickname());
		log.debug("FreeBoard : {}", fBoard);
		
		/* 여기까지 실행 완료 */
		
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

	/*
	@GetMapping("/display")
	// get값으로 들어온 num23을 쓸거라 pathvariable
	public List<FileDTO> display(int num) {
		log.debug("num : {}", num);

		// 파일과 경로 가져오기
		// 파일정보가 담긴 리스트
		
		//파일자체를 줘버려서 해당 경로값 뽑아버리기
		return fService.getFileList(num);
		}
		// log.debug("파일리스트사이즈 : {}",fileList.size());
		
		// 진짜 파일 읽어오기
		 
		List<Resource> resource = null; 
		for ( int i = 0; i < fileList.size(); i++) {
			resource.add(new FileSystemResource(uploadPath + "/" + fileList.get(i).getSaveName()));
		}
		// 파일이 존재하지 않을 때
		if ( resource.size()==0) {
			// 404 요소 없음
			 return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
			// return new ResponseEntity<Resource>(HttpStatus.OK);
			
		}
		
		HttpHeaders header = new HttpHeaders();
		// List<HttpHeaders> header = (List<HttpHeaders>) new HttpHeaders();
		
		// 경로를 가져오기 
		List<Path> filePath = null;
		
		try {
			// 파일 경로
			// 첨부한 내용의 타입은 파일
			for ( int i = 0; i < fileList.size(); i++) {
				filePath.add(Paths.get(uploadPath + "/" + fileList.get(i).getSaveName()));
				header.add("Content-type", Files.probeContentType(filePath.get(i)));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
	}
	 */
	
	
}
