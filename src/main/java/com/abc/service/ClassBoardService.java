package com.abc.service;


import java.util.List;
import java.util.Map;

import com.abc.domain.ClassBoard;
import com.abc.domain.ClassRoom;
import com.abc.util.PageNavigator;

public interface ClassBoardService {

	public int insertClassBoard(ClassBoard cb);


	public PageNavigator getPageNavigator(int pagePerGroup, int countPerPage, int Page);
	public List<ClassBoard> selectAllClassBoard(PageNavigator navi);
	public List<ClassBoard> selectAllClassBoardNoParameter();
	
	public ClassBoard selectOneClassBoard(int classNum);
	
	public int insertClassRoom(ClassRoom cRoom);
	public List<ClassRoom> selectClassRoom(int classNum);
	public ClassRoom selectClassRoomByMemberNumAndClassNum(int memberNum, int classNum);
	public int withdrawalParty(int memberNum, int classNum);


}
