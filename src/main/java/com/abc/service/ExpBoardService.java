package com.abc.service;

import java.util.List;

import com.abc.domain.ExpBoard;

public interface ExpBoardService {
	
	public int insertExpBoard(ExpBoard exp);

	public List<ExpBoard> getLevelList();
}