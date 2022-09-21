package com.abc.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.dao.ChatDAO;
import com.abc.domain.ChatRoom;
import com.abc.domain.MyChatRoom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private Map<String, ChatRoom> chatRooms;

    @Autowired
    private ChatDAO chatDao; 
    
    @PostConstruct
    //의존관게 주입완료되면 실행되는 코드
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    //채팅방 불러오기
    public List<ChatRoom> findAllRoom(String search) {
        //채팅방 최근 생성 순으로 반환
        Map<String, String> map = new HashMap<String, String>();
        map.put("searchKeyword", search);
        
    	List<ChatRoom> result = chatDao.findAllRoom(map);
        return result;
    }

    //채팅방 하나 불러오기
    public ChatRoom findById(String roomId) {
    	
    	ChatRoom chatRoom = chatDao.findOneRoom(roomId);
    	
    	log.debug("chatRoom : {}", chatRoom.getRoomId());
        return chatRoom;
    }

    //채팅방 생성
    public ChatRoom createRoom(String name) {
        ChatRoom chatRoom = ChatRoom.create(name);
        Map<String, String> map = new HashMap<String, String>();
        map.put("roomId", chatRoom.getRoomId());
        map.put("roomName", chatRoom.getRoomName());
        chatDao.insertChatRoom(map);
        log.debug("chatRooms : {}", map);
        return chatRoom;
    }
    
    // 채팅방 지우기
    public int deleteRoom(String roomId) {
    	int result = chatDao.deleteRoom(roomId);
    	return result;
    }
    
    // 내 채팅방에 추가
    public int insertMyChatRoom(MyChatRoom myChatRoom) {
    	int result = chatDao.insertMyChatRoom(myChatRoom);
    	return result;
    }
    
}