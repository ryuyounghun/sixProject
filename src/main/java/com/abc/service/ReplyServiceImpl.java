package com.abc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.dao.ReplyDAO;
import com.abc.domain.Reply;

@Service
public class ReplyServiceImpl implements ReplyService {

	
	@Autowired
	private ReplyDAO rDAO;
	
	@Override
	public int insertReply(Reply reply) {
		return rDAO.insertReply(reply);
	}

	@Override
	public Reply selectOneReply(int replyNum) {
		return rDAO.selectOneReply(replyNum);
	}

	@Override
	public List<Reply> selectReplyByBoardNum(int boardNum) {
		return rDAO.selectReplyByBoardNum(boardNum);
	}

	@Override
	public int deleteReply(int replyNum) {
		return rDAO.deleteReply(replyNum);
	}

	@Override
	public int updateReply(Reply reply) {
		return rDAO.updateReply(reply);
	}
}
