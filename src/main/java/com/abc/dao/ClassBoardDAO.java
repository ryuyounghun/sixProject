package com.abc.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.abc.domain.ClassBoard;
import com.abc.domain.FileDTO;

@Mapper
public interface ClassBoardDAO {

	public int insertClassBoard(ClassBoard cb);
	
	public List<ClassBoard> selectAllClassBoard(RowBounds rb);
	public int countAllClassBoard(Map<String, String> map);

}
