package com.abc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.dao.DeliveryDAO;
import com.abc.domain.Item;
import com.abc.domain.Order;
import com.abc.domain.Store;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DeliveryServiceImpl implements DeliveryService{

	@Autowired
	private DeliveryDAO dDao;
	
	@Override
	public int insertStore(Store store) {
		// TODO Auto-generated method stub
		return dDao.insertStore(store);
	}

	@Override
	public List<Store> selectSearch(String keyword, String category) {
		// TODO Auto-generated method stub
		
		Map<String, String> map = new HashMap<String, String>();
		
		log.debug("category : {}", category);
		log.debug("keyword : {}", keyword);
		
		map.put("category", category);
		map.put("searchKeyword", keyword);
		
		return dDao.selectSearch(map);
	}

	@Override
	public Store selectOne(int num) {
		// TODO Auto-generated method stub
		return dDao.selectOne(num);
	}

	@Override
	public List<Store> selectMemberOne(int num) {
		// TODO Auto-generated method stub
		return dDao.selectMemberOne(num);
	}
	
	@Override
	public int insertItem(Item item) {
		// TODO Auto-generated method stub
		return dDao.insertItem(item);
	}

	@Override
	public List<Item> selectItemSearch(String title, String storeNum) {

		Map<String, String> map = new HashMap<String, String>();
		
		log.debug("title : {}", title);
		log.debug("storeNum : {}", storeNum);
		
		map.put("title", title);
		map.put("storeNum", storeNum);
		
		return dDao.selectItemSearch(map);
	}

	@Override
	public Item selectOneItem(int num) {
		// TODO Auto-generated method stub
		return dDao.selectOneItem(num);
	}

	@Override
	public int insertOrder(Order order) {
		// TODO Auto-generated method stub
		return dDao.insertOrder(order);
	}

	@Override
	public List<Order> selectOrder(Order order) {
		// TODO Auto-generated method stub
		return dDao.selectOrder(order);
	}

	@Override
	public int plusOrder(Order order) {
		// TODO Auto-generated method stub
		return dDao.plusOrder(order);
	}


}
