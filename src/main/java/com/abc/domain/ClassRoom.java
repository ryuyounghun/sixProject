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
public class ClassRoom {

	
	private int roomNum;
	private int classNum;
	private int memberNum;
	private String nickname;
	private String address;
	
	// 레벨용 아이디
	private int memberLevel;

	private String originalFile;	 
	private String savedFile;
}
