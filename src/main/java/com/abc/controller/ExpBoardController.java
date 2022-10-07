package com.abc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.abc.domain.ExpBoard;
import com.abc.service.ExpBoardService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("exp")
public class ExpBoardController {

	@Autowired
	private ExpBoardService expService;
	
	@GetMapping("/insertExp")
	public String insertExp() {
		log.debug("insertExp() 실행");
		return "exp/insertExp";
	}
	
	@PostMapping("/insertExp")
	public String insertExp(ExpBoard exp) {
		
		log.debug("insertExpBoard : {}", exp);
		expService.insertExpBoard(exp);
		
		return "exp/insertExp";
	}
}
