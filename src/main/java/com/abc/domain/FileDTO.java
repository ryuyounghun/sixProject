package com.abc.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileDTO {

	/** 파일 번호 (PK) */
	private long fileNum;

	/** 게시글 번호 (FK) */
	private int boardNum;

	/** 원본 파일명 */
	private String originalName;

	/** 저장 파일명 */
	private String saveName;

	/** 파일 크기 */
	private long fileSize;
}
