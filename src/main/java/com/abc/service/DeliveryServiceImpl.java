package com.abc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.dao.DeliveryDAO;
import com.abc.domain.Store;

@Service
public class DeliveryServiceImpl implements DeliveryService{

	@Autowired
	private DeliveryDAO dDao;
	
	@Override
	public int insertStore(Store store) {
		// TODO Auto-generated method stub
		return dDao.insertStore(store);
	}

}
