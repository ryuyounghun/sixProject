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
public class Reply {

	private int replyNum;
	private int boardNum;
	private String memberNum;
	private String nickname;
	private String content;
	private String inputdate;
}
