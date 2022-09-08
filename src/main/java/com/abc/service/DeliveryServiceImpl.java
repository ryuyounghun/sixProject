package com.abc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.dao.DeliveryDAO;
import com.abc.domain.Item;
import com.abc.domain.Order;
import com.abc.domain.Receipt;
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
	public List<Order> selectMyOrder(int memberNum, int storeNum) {
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		
		log.debug("memberNum : {}", memberNum);
		log.debug("storeNum : {}", storeNum);
		
		map.put("memberNum", memberNum);
		map.put("storeNum", storeNum);
		
		return dDao.selectMyOrder(map);
	}

	@Override
	public int plusOrder(Order order) {
		// TODO Auto-generated method stub
		return dDao.plusOrder(order);
	}

	@Override
	public Order selectOneOrder(int num) {
		// TODO Auto-generated method stub
		return dDao.selectOneOrder(num);
	}

	@Override
	public int minusOrder(Order order) {
		// TODO Auto-generated method stub
		return dDao.minusOrder(order);
	}

	@Override
	public int paymentOrder(int memberNum, int storeNum) {
		// TODO Auto-generated method stub
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		
		log.debug("memberNum : {}", memberNum);
		log.debug("storeNum : {}", storeNum);
		
		map.put("memberNum", memberNum);
		map.put("storeNum", storeNum);
		
		return dDao.paymentOrder(map);
	}

	@Override
	public int deleteOrder(int num) {
		// TODO Auto-generated method stub
		return dDao.deleteOrder(num);
	}

	@Override
	public List<Order> selectMyOrders(int memberNum) {
		// TODO Auto-generated method stub
		return dDao.selectMyOrders(memberNum);
	}

	@Override
	public int insertReceipt(Receipt receipt) {
		// TODO Auto-generated method stub
		return dDao.insertReceipt(receipt);
	}

	@Override
	public List<Receipt> selectReceipt(int memberNum) {
		// TODO Auto-generated method stub
		return dDao.selectReceipt(memberNum);
	}


}
