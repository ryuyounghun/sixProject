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

	// 등록
	public int insertFreeBoard(FreeBoard fb);
	
	// 수정
	public int updateFreeBoard(FreeBoard fb);
	
	// 지우기
	public int deleteBoard(Long boardNum);
	
	// 선택하기
	public FreeBoard selectOneFreeBoard(int boardNum);
	
	public FreeBoard selectBoardDetail(int boardNum);
	

	public List<FreeBoard> selectBoardList(FreeBoard bf);

	public int selectBoardTotalCount(FreeBoard bf);

	 public List<FreeBoard> selectAllFreeBoard(RowBounds rb);
	 public int countAllFreeBoard(Map<String, String> map);
}
