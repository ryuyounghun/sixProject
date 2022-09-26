package com.abc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.dao.MasterDAO;
import com.abc.domain.BannedMember;
import com.abc.domain.Coupon;
import com.abc.domain.Member;
import com.abc.domain.MessageReport;
import com.abc.domain.MyCoupon;

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

	@Override
	public int insertMyCoupon(MyCoupon myCoupon) {
		// TODO Auto-generated method stub
		return mtDao.insertMyCoupon(myCoupon);
	}

	@Override
	public List<MyCoupon> selectAllMyCoupon(int memberNum) {
		// TODO Auto-generated method stub
		return mtDao.selectAllMyCoupon(memberNum);
	}

	@Override
	public MyCoupon useOneMyCoupon(int myCouponNum) {
		// TODO Auto-generated method stub
		return mtDao.useOneMyCoupon(myCouponNum);
	}

	@Override
	public int deleteOneMyCoupon(int myCouponNum) {
		// TODO Auto-generated method stub
		return mtDao.deleteOneMyCoupon(myCouponNum);
	}
	
	@Override
	public int banMember(String memberId) {
		return mtDao.banMember(memberId);
	}

	@Override
	public List<Member> selectAllBannedList() {
		return mtDao.selectAllBannedList();
	}

	@Override
	public MessageReport selectOneReport(int reportNum) {
		return mtDao.selectOneReport(reportNum);
	}

	@Override
	public int insertBanListMember(BannedMember bm) {
		return mtDao.insertBanListMember(bm);
	}

	@Override
	public int unBanMember(String memberId) {
		return mtDao.unBanMember( memberId);
	}

	@Override
	public int deleteBanMember(String memberId) {
		return mtDao.deleteBanMember(memberId);
	}
}
