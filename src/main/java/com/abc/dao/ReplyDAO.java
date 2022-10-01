package com.abc.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.abc.domain.Reply;

@Mapper
public interface ReplyDAO {

	public int insertReply(Reply reply);
	public Reply selectOneReply( int replyNum);
	public List<Reply> selectReplyByBoardNum( int boardNum);
	public int deleteReply(int replyNum);
	public int updateReply(Reply reply);
	
}
