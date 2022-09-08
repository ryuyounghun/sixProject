package com.abc.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.abc.domain.ClassBoard;
import com.abc.domain.FileDTO;
import com.abc.domain.FreeBoard;

@Mapper
public interface FreeBoardDAO {

	public int insertFreeBoard(FreeBoard fb);
	public int updateFreeBoard(FreeBoard fb);
	
	public FreeBoard selectOneFreeBoard(Long boardNum);
	
	public FreeBoard selectBoardDetail(Long boardNum);
	
	public int deleteBoard(Long boardNum);

	public List<FreeBoard> selectBoardList(FreeBoard bf);

	public int selectBoardTotalCount(FreeBoard bf);

	/*
	 public List<FreeBoard> selectAllFreeBoard(RowBounds rb); public int
	 countAllFreeBoard(Map<String, String> map);
	 */
}
