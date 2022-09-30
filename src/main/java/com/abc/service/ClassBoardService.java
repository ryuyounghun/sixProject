package com.abc.service;


import java.util.List;

import com.abc.domain.ClassBoard;
import com.abc.domain.ClassRoom;
import com.abc.util.PageNavigator;

public interface ClassBoardService {

	public int insertClassBoard(ClassBoard cb);


	// 검색어 없음
	public PageNavigator getPageNavigator(int pagePerGroup, int countPerPage, int Page);
	public List<ClassBoard> selectAllClassBoard(PageNavigator navi);

	// 메인 인덱스용 페이지 네비게이터
	public PageNavigator getPageNavigator(int pagePerGroup, int countPerPage, int Page, String searchWord);
	// 검색어 있음
	public List<ClassBoard> selectAllClassBoard(PageNavigator navi, String searchWord);

	// 파라미터 없이 다 추력
	public List<ClassBoard> selectAllClassBoardNoParameter();
	
	public ClassBoard selectOneClassBoard(int classNum);
	
	public int insertClassRoom(ClassRoom cRoom);
	public List<ClassRoom> selectClassRoom(int classNum);
	public ClassRoom selectClassRoomByMemberNumAndClassNum(int memberNum, int classNum);
	public int withdrawalParty(int memberNum, int classNum);
}
