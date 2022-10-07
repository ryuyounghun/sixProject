package com.abc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.dao.CouponDAO;
import com.abc.domain.Coupon;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service

public class CouponServiceImpl implements CouponService{
	
	@Autowired CouponDAO cpDAO;
	
	public int insertCoupon(Coupon cp) {
		return cpDAO.insertCoupon(cp);
	}

}
