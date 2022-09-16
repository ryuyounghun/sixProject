package com.abc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.dao.ExpBoardDAO;
import com.abc.domain.ExpBoard;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExpBoardServiceImpl implements ExpBoardService{
	
	@Autowired
	private ExpBoardDAO expDAO;

	@Override
	public int insertExpBoard(ExpBoard exp) {
		return expDAO.insertExpBoard(exp);
	}
	
}
