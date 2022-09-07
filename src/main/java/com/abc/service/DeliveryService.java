package com.abc.service;

import java.util.List;

import com.abc.domain.Store;

public interface DeliveryService {

	public int insertStore(Store store);
	public List<Store> selectSearch(String category, String keyword);
}
