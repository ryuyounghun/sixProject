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
public class Receipt {

	private int receiptNum;
	private int memberNum;
	private int storeNum;
	private String orderHistory;
	private int totalAmount;
	private String orderAddress;
	private String waiting;
	private String complete;
}
