package com.abc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.dao.ClassBoardDAO;
import com.abc.domain.ClassBoard;
import com.abc.domain.ClassRoom;
import com.abc.util.PageNavigator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClassBoardServiceImpl implements ClassBoardService{

	@Autowired
	private ClassBoardDAO cDAO;

	
	@Override
	public PageNavigator getPageNavigator(int pagePerGroup, int countPerPage, int Page) {
		Map<String, String> map = new HashMap<>();
		
		int total= cDAO.countAllClassBoard(map);
		
		PageNavigator navi =
				new PageNavigator(pagePerGroup, countPerPage, Page, total);
		
		return navi;
	}
	
	@Override
	public PageNavigator getPageNavigator(int pagePerGroup, int countPerPage, int Page, String searchWord) {
		
		Map<String, String> map = new HashMap<>();
		map.put("searchWord", searchWord);
		
		int total = cDAO.countAllClassBoard(map);
		
		PageNavigator navi =
				new PageNavigator(pagePerGroup, countPerPage, Page, total);
		
		return navi;
	}
	@Override
	public List<ClassBoard> selectAllClassBoardNoParameter() {
		// TODO Auto-generated method stub
		return cDAO.selectAllClassBoardNoParameter();
	}
	
	
	@Override
	public int insertClassBoard(ClassBoard cb) {

		return cDAO.insertClassBoard(cb);
	}
	

	@Override
	public ClassBoard selectOneClassBoard(int classNum) {
		// TODO Auto-generated method stub
		return cDAO.selectOneClassBoard(classNum);
	}

	@Override
	public int insertClassRoom(ClassRoom cRoom) {
		// TODO Auto-generated method stub
		return cDAO.insertClassRoom(cRoom);
	}

	@Override
	public List<ClassRoom> selectClassRoom(int classNum) {
		// TODO Auto-generated method stub
		return cDAO.selectClassRoom(classNum);
	}

	@Override
	public ClassRoom selectClassRoomByMemberNumAndClassNum(int memberNum, int classNum) {
		// TODO Auto-generated method stub
		Map<Object, Object> map = new HashMap<>();
		
		map.put("memberNum", memberNum);
		map.put("classNum", classNum);
		
		return cDAO.selectClassRoomByMemberNumAndClassNum(map);
	}

	@Override
	public int withdrawalParty(int memberNum, int classNum) {
		// TODO Auto-generated method stub
		Map<Object, Object> map = new HashMap<>();
		
		map.put("memberNum", memberNum);
		map.put("classNum", classNum);
		
		return cDAO.withdrawalParty(map);
	}

	@Override
	public ClassRoom selectClassRoomByMemberNum(int memberNum) {
		// TODO Auto-generated method stub
		return cDAO.selectClassRoomByMemberNum(memberNum);
	}

	@Override
	public int deleteClassBoard(int classNum) {
		// TODO Auto-generated method stub
		return cDAO.deleteClassBoard(classNum);
	}
	
	@Override
	public List<ClassBoard> selectAllClassBoard(PageNavigator navi, String searchWord) {
		Map<String, String> map = new HashMap<>();
		map.put("searchWord", searchWord);
		
		RowBounds rb = new RowBounds(navi.getStartRecord(), navi.getCountPerPage());
		
		return cDAO.selectAllClassBoard(map, rb);
	}

	@Override
	public List<ClassBoard> selectAllClassBoard(PageNavigator navi) {
		Map<String, String> map = new HashMap<>();
		// "?????? ?????????"?????? "??? ???????????? ???" ???????????? ??????
		RowBounds rb = new RowBounds(navi.getStartRecord(), navi.getCountPerPage());
		return cDAO.selectAllClassBoard(map, rb);
	}

	@Override
	public int updateViewCount(int classNum) {
		// TODO Auto-generated method stub
		return cDAO.updateViewCount(classNum);
	}

	@Override
	public List<ClassBoard> selectClassBoardRank() {
		// TODO Auto-generated method stub
		return cDAO.selectClassBoardRank();
	}


}
