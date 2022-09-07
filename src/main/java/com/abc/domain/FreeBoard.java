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
public class FreeBoard {

	private long boardNum;
	private String memberNum;
	private String nickname;
	private String category;
	private String title;
	private String content;
	private String inputdate;
	
	private String originalFile;	 
	private String savedFile;
}
