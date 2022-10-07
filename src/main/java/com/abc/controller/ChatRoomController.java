package com.abc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.abc.domain.ChatRoom;
import com.abc.domain.Member;
import com.abc.service.ChatService;
import com.abc.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatService chatService;

    @Autowired
    private MemberService mService;
    
    // 채팅 리스트 화면
    @GetMapping("/room")
    public String rooms(Model model, @AuthenticationPrincipal UserDetails user) {
    	Member member = mService.selectOneMember(user.getUsername());
    	model.addAttribute("member", member);
    	
        return "chat/room";
    }
    // 모든 채팅방 목록 반환
    @PostMapping("/rooms")
    @ResponseBody
    public List<ChatRoom> room(@RequestParam String search) {
    	List<ChatRoom> list = null;
    	
    	log.debug("search : {}", search);
    	
    	if (search == null) {
    		list = chatService.findAllRoom(null);
		} else {
			list = chatService.findAllRoom(search);
		}
    	
    	log.debug("list : {}", list.toString());
        return list;
    }
    
    // 채팅방 생성
    @PostMapping("/room")
    @ResponseBody
    public ChatRoom createRoom(@RequestParam String name,
    		@AuthenticationPrincipal UserDetails user) {
    	log.debug("name : {}" , name);
    	Member member = mService.selectOneMember(user.getUsername());
    	ChatRoom ctRoom = chatService.createRoom(name);
    	
        return ctRoom;
    }
    @PostMapping("/deleteRoom")
    @ResponseBody
    public String deleteRoom(@RequestParam String roomId) {
    	log.debug("roomId : {}", roomId);
    	chatService.deleteRoom(roomId);
    	return "dd";
    }
    // 채팅방 입장 화면
    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId,
    		@AuthenticationPrincipal UserDetails user) {
    	
    	Member member = mService.selectOneMember(user.getUsername());
    	
        model.addAttribute("member", member);
        
        return "/chat/roomdetail";
    }
    
    
    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatService.findById(roomId);
    }
}