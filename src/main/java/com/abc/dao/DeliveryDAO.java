package com.abc.dao;

import org.apache.ibatis.annotations.Mapper;

import com.abc.domain.Store;

@Mapper
public interface DeliveryDAO {
	
	public int insertStore(Store store);

}
