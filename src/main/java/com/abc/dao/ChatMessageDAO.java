package com.abc.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.abc.domain.ChatRoom;
import com.abc.domain.MessageReport;
import com.abc.domain.MyChatRoom;


@Mapper
public interface ChatMessageDAO {

	// 신고하기 
	public int insertReportMessage(MessageReport mr);
	
	// 신고기록 다 가져오기
	public List<MessageReport> selectAllReportMessage();

	
}
