package com.abc.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageReport {
	
	private int reportNum;
	private String sender;
	private String message;
	private String reporter;
	private String roomId;
	private String inputdate;
	
}
