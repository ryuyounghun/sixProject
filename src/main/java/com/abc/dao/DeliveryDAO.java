package com.abc.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.abc.domain.Store;

@Mapper
public interface DeliveryDAO {
	
	public int insertStore(Store store);
	public List<Store> selectSearch(Map<String, String> map);
}
