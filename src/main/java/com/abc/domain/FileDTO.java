package com.abc.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileDTO {

	/** 파일 번호 (PK) */
	private Long fileNum;

	/** 게시글 번호 (FK) */
	private Long boardNum;

	/** 원본 파일명 */
	private String originalFile;

	/** 저장 파일명 */
	private String savedFile;

	/** 파일 크기 */
	private long fileSize;
}
