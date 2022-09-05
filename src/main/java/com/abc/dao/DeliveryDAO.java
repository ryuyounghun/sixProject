package com.abc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.abc.domain.Store;

@Mapper
public interface DeliveryDAO {
	
	public int insertStore(Store store);
	public List<Store> selectSearch(String searchKeyword);
}
