package com.abc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.abc.domain.Coupon;

@Mapper
public interface MasterDAO {
	public int insertCoupon(Coupon cp);
	public List<Coupon> selectAllCoupon();
	public Coupon selectCoupon(int couponNum);
}
