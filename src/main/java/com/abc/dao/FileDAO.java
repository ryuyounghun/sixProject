package com.abc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.abc.domain.FileDTO;

@Mapper
public interface FileDAO {

	
	
	public int insertFile(FileDTO fd);

	public FileDTO selectFileDetail(Long fileNum);

	public int deleteFile(Long boardNum);

	public List<FileDTO> selectFileList(Long boardNum);

	public int selectFileTotalCount(Long boardNum);
	
}
