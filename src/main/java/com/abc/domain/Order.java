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
public class Order {

	private int orderNum;
	private int memberNum;
	private int itemNum;
	private int storeNum;
	private String itemName;
	private int itemPrice;
	private int quantity;
	private String waiting;
	private String orderComplete;
}
