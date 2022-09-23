package com.abc.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.abc.domain.ClassBoard;
import com.abc.domain.ClassRoom;

@Mapper
public interface ClassBoardDAO {

	public int insertClassBoard(ClassBoard cb);
	
	public List<ClassBoard> selectAllClassBoard(RowBounds rb);
	public int countAllClassBoard(Map<String, String> map);
	public List<ClassBoard> selectAllClassBoardNoParameter();
	
	public ClassBoard selectOneClassBoard(int classNum);
	// 0923 추가
	public int deleteClassBoard(int classNum);
	
	public int insertClassRoom(ClassRoom cRoom);
	public List<ClassRoom> selectClassRoom(int classNum);
	public ClassRoom selectClassRoomByMemberNumAndClassNum(Map<Object, Object> map);
	public int withdrawalParty(Map<Object, Object> map);
	public ClassRoom selectClassRoomByMemberNum(int memberNum);
}
