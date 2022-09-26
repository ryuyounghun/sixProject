package com.abc.service;

import java.util.List;

import com.abc.domain.MessageReport;

public interface ChatMessageService {

	public int insertReportMessage(MessageReport mr);


	// 신고기록 다 가져오기
	public List<MessageReport> selectAllReportMessage();

}
