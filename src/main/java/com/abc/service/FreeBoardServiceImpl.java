package com.abc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.abc.dao.FileDAO;
import com.abc.dao.FreeBoardDAO;
import com.abc.domain.ClassBoard;
import com.abc.domain.FileDTO;
import com.abc.domain.FreeBoard;
import com.abc.exception.AttachFileException;
import com.abc.util.FileUtils;
import com.abc.util.PageNavigator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FreeBoardServiceImpl implements FreeBoardService{

	@Autowired
	private FreeBoardDAO fbDAO;

	@Autowired
	private FileDAO fileDAO;

	@Autowired
	private FileUtils fileUtils;

	@Override
	public int registerBoard(FreeBoard fb) {
		
		return fbDAO.insertFreeBoard(fb);
	}

	@Override
	public boolean registerBoard(FreeBoard fb, MultipartFile[] files) {
		
		if (fb == null) {
			return false;
		}
		
		int result = 1;
	
		
		List<FileDTO> fileList = fileUtils.uploadFiles(files);
		
		fbDAO.insertFreeBoard(fb);
		for ( int i = 0; i<fileList.size(); i++ ) {
			fileDAO.insertFile(fileList.get(i));
			log.debug(fileList.get(i).toString());
		}	
		
		return result > 0;
	}

	@Override
	public FreeBoard getBoardDetail(Long fileNum) {
		return fbDAO.selectBoardDetail(fileNum);
	}

	@Override
	public int deleteBoard(Long fileNum) {
			
		return fbDAO.deleteBoard(fileNum);
	}

	@Override
	public List<FreeBoard> getBoardList(FreeBoard params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int selectOneFreeBoard(Long boardNum) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public PageNavigator getPageNavigator(int pagePerGroup, int countPerPage, int Page) {
		Map<String, String> map = new HashMap<>();
		
		int total= fbDAO.countAllFreeBoard(map);
		
		PageNavigator navi =
				new PageNavigator(pagePerGroup, countPerPage, Page, total);
		
		return navi;
	}

	@Override
	public List<FreeBoard> selectAllFreeBoard(PageNavigator navi) {
		Map<String, String> map = new HashMap<>();

		// "시작 레코드"부터 "한 페이지의 글" 단위만큼 선택
		RowBounds rb = new RowBounds(navi.getStartRecord(), navi.getCountPerPage());
		
		return fbDAO.selectAllFreeBoard(rb);
	}


}
