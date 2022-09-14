package com.abc.service;

import java.util.List;

import com.abc.domain.Reply;

public interface ReplyService {

	public int insertReply(Reply reply);
	
	public Reply selectOneReply( int replyNum);
	
	public List<Reply> selectReplyByBoardNum( int num);
	
	public int deleteReply(int replyNum);

	public int updateReply(Reply reply);
}
