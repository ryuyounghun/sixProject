package com.abc.dao;

import org.apache.ibatis.annotations.Mapper;

import com.abc.domain.Coupon;

@Mapper
public interface CouponDAO {
	public int insertCoupon(Coupon cp);
}
