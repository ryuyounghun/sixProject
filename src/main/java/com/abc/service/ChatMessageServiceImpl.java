package com.abc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.dao.ChatMessageDAO;
import com.abc.dao.ClassBoardDAO;
import com.abc.domain.MessageReport;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChatMessageServiceImpl implements ChatMessageService {

	@Autowired
	private ChatMessageDAO cmDAO;

	
	// 신고 테이블에 담기
	@Override
	public int insertReportMessage(MessageReport mr) {
		log.debug("신고 정보impl");
		log.debug("mr :  {}",mr.toString());
		
		return cmDAO.insertReportMessage(mr);
	}


	@Override
	public List<MessageReport> selectAllReportMessage() {
		return cmDAO.selectAllReportMessage();
	}
	
}
