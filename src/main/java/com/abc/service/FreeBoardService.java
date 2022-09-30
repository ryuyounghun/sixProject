package com.abc.service;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.abc.domain.ClassBoard;
import com.abc.domain.FileDTO;
import com.abc.domain.FreeBoard;
import com.abc.util.PageNavigator;

public interface FreeBoardService {

	// 글등록하기
	public int registerBoard(FreeBoard fb);
	public boolean registerBoard(FreeBoard fb, MultipartFile[] files);
	public boolean registerBoard(int num, MultipartFile[] files);
	// freeboard객체 하나 가져오기
	public FreeBoard selectOneFreeBoard(int boardNum);
	
	// 해당 boardNum에 해당하는 파일 가져오기
	public List<FileDTO> selectFileList(int num);

	// 파일넘에 해당하는 파일 가져오기
	public FileDTO selectOneFile(Long fileNum);

	public int deleteBoard(Long fileNum);

	public List<FreeBoard> getBoardList(FreeBoard fb);

	
	public PageNavigator getPageNavigator(int pagePerGroup, int countPerPage, int Page);
	public List<FreeBoard> selectAllFreeBoard(PageNavigator navi);

	
	
	// 0929 추가 수정기능
	public int updateFreeBoard(FreeBoard fb);
	
	// 0930 추가
	public int updateViewCount(int boardNum);
	public List<FreeBoard> selectFreeBoardRank();
}
