package com.abc.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.abc.domain.ChatMessage;
import com.abc.domain.ChatRoom;


@Mapper
public interface ChatDAO {

	// 채팅방 생성
	public int insertChatRoom(Map<String, String> map);
	
	// 채팅방 출력
	public List<ChatRoom> findAllRoom(Map<String, String> map);
	
	// 채팅방 지우기
	public int deleteRoom(String roomId);
	
	// 한 채팅방 찾기
	public ChatRoom findOneRoom(String roomId);
	
	// 0923 추가 
	public int deleteOneRoom(String roomId);
	
}
