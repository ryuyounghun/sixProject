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
public class ExpBoard {

	private int userLevel; 	// 사용자 레벨
	private int userExp;	// 사용자 경험치
	
}
