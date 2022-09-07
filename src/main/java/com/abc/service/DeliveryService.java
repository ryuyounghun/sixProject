package com.abc.service;

import java.util.List;
import java.util.Map;

import com.abc.domain.Item;
import com.abc.domain.Order;
import com.abc.domain.Store;

public interface DeliveryService {

	public int insertStore(Store store);
	public List<Store> selectSearch(String category, String keyword);
	public Store selectOne(int num);
	
	public List<Store> selectMemberOne(int num);
	
	public int insertItem(Item item);
	public List<Item> selectItemSearch(String title, String storeNum);
	public Item selectOneItem(int num);
	
	public int insertOrder(Order order);
	public List<Order> selectOrder(Order order);
	public int plusOrder(Order order);
}
