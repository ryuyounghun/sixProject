package com.abc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ClassBoard {

	private int classNum;
	private String classTag;
	private int memberNum;
	private String destination;
	private int totalMember;
	private String nickname;
	private String title;
	private String content;
	private String inputdate;
	private String roomId;
	
	private String originalFile;	 
	private String savedFile;
	
	// 레벨용 아이디
	private int memberLevel;
}
