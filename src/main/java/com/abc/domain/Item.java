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
public class Item {

	private int itemNum;
	private int storeNum;
	private String itemName;
	private String itemContent;
	private int itemPrice;
	
	private String originalFile;	 
	private String savedFile;
}
