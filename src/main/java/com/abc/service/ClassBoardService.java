package com.abc.service;


import java.util.List;

import com.abc.domain.ClassBoard;
import com.abc.util.PageNavigator;

public interface ClassBoardService {

	public int insertClassBoard(ClassBoard cb);


	public PageNavigator getPageNavigator(int pagePerGroup, int countPerPage, int Page);
	public List<ClassBoard> selectAllClassBoard(PageNavigator navi);


}
