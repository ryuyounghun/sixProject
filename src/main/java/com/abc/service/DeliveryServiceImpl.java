package com.abc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.dao.DeliveryDAO;
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

}
