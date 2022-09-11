package com.abc.service;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.abc.domain.ClassBoard;
import com.abc.domain.FreeBoard;
import com.abc.util.PageNavigator;

public interface FreeBoardService {

	public int registerBoard(FreeBoard fb);
	public int selectOneFreeBoard(Long boardNum);
	public boolean registerBoard(FreeBoard fb, MultipartFile[] files);

	public FreeBoard getBoardDetail(Long fileNum);

	public int deleteBoard(Long fileNum);

	public List<FreeBoard> getBoardList(FreeBoard fb);

	
	public PageNavigator getPageNavigator(int pagePerGroup, int countPerPage, int Page);
	public List<FreeBoard> selectAllFreeBoard(PageNavigator navi);

}
