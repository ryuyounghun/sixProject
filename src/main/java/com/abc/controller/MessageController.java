package com.abc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import com.abc.domain.ChatMessage;
import com.abc.domain.Member;
import com.abc.domain.MessageReport;
import com.abc.service.ChatMessageService;
import com.abc.service.ChatService;
import com.abc.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessageSendingOperations sendingOperations;

    
    @Autowired
    private ChatMessageService cmService;
    
    @Autowired
    private MemberService mService;
    
    @MessageMapping("/chat/message")
    public void enter(ChatMessage message) {
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender()+"님이 입장하였습니다.");
        }
        log.debug("messageID : {}", message.getMessage());
        
        
        sendingOperations.convertAndSend("/topic/chat/room/"+message.getRoomId(),message);
    }
    
    @MessageMapping("/chat/report")
    public String reportMessage(MessageReport mr) {
    	log.debug("신고정보");
    	log.debug(mr.getReporter());
    	log.debug(mr.getMessage());
    	log.debug(mr.getSender());
    	log.debug(mr.getRoomId());
    	log.debug("========");
    	
    	cmService.insertReportMessage(mr);
    	
    	return "";
    }
}