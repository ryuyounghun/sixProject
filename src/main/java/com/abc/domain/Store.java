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
public class Store {

	private int storeNum;
	private String memberNum;
	private String storeName;
	private String storeContent;
	private String category;
	private String address;
	private String phone;
	private int rating;
	private int wishlist;
	
	private String originalFile;	 
	private String savedFile;
}
