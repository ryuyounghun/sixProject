package com.abc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.abc.domain.ExpBoard;

@Mapper
public interface ExpBoardDAO {

	public int insertExpBoard(ExpBoard exp);
	
	public List<ExpBoard> getLevelList();
}
