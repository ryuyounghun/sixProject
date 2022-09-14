package com.abc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.dao.MasterDAO;
import com.abc.domain.Coupon;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service

public class MasterServiceImpl implements MasterService{
	
	@Autowired private MasterDAO mtDao;
	
	public int insertCoupon(Coupon cp) {
		return mtDao.insertCoupon(cp);
	}

	@Override
	public List<Coupon> selectAllCoupon() {
		// TODO Auto-generated method stub
		return mtDao.selectAllCoupon();
	}

	@Override
	public Coupon selectCoupon(int couponNum) {
		// TODO Auto-generated method stub
		return mtDao.selectCoupon(couponNum);
	}

}
