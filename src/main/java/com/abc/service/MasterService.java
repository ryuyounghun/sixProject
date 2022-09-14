package com.abc.service;

import java.util.List;

import com.abc.domain.Coupon;

public interface MasterService {
	public int insertCoupon(Coupon cp);
	public List<Coupon> selectAllCoupon();
	public Coupon selectCoupon(int couponNum);
}
