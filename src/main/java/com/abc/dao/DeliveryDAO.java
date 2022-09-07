package com.abc.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.abc.domain.Item;
import com.abc.domain.Order;
import com.abc.domain.Store;

@Mapper
public interface DeliveryDAO {
	
	// 가게 등록 및 출력 부분
	public int insertStore(Store store);
	public List<Store> selectSearch(Map<String, String> map);
	public Store selectOne(int num);
	
	public List<Store> selectMemberOne(int num);
	
	// 상품 등록 및 출력부분
	public int insertItem(Item item);
	public List<Item> selectItemSearch(Map<String, String> map);
	public Item selectOneItem(int num);
	
	// 주문 등록 및 출력부분
	public int insertOrder(Order order);
	public List<Order> selectOrder(Order order);
	public int plusOrder(Order order);
}
