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
public class Review {

	private int reviewNum;
	private int memberNum;
	private int storeNum;
	private int receiptNum;
	private int rating;
	private String reviewContent;
}
