package com.abc.service;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.abc.domain.FreeBoard;

public interface FreeBoardService {

	public boolean registerBoard(FreeBoard fb);

	public boolean registerBoard(FreeBoard fb, MultipartFile[] files);

	public FreeBoard getBoardDetail(Long fileNum);

	public int deleteBoard(Long fileNum);

	public List<FreeBoard> getBoardList(FreeBoard fb);

}
