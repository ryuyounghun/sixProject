package com.abc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.abc.domain.Coupon;
import com.abc.domain.MyCoupon;

@Mapper
public interface MasterDAO {
	public int insertCoupon(Coupon cp);
	public List<Coupon> selectAllCoupon();
	public Coupon selectCoupon(int couponNum);
	public int insertMyCoupon(MyCoupon myCoupon);
	public List<MyCoupon> selectAllMyCoupon(int memberNum);
	public MyCoupon useOneMyCoupon(int myCouponNum);
	public int deleteOneMyCoupon(int myCouponNum);
}
