package com.abc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.abc.domain.FileDTO;

@Mapper
public interface FileDAO {

	
	
	public int insertFile(FileDTO fd);

	public FileDTO selectOneFile(Long fileNum);

	public int deleteFile(int boardNum);

	public List<FileDTO> selectFileList(int boardNum);

	public int selectFileTotalCount(int boardNum);
	
}
