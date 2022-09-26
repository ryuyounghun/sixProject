package com.abc.service;

import java.util.List;

import com.abc.domain.BannedMember;
import com.abc.domain.Coupon;
import com.abc.domain.Member;
import com.abc.domain.MessageReport;
import com.abc.domain.MyCoupon;

public interface MasterService {
	public int insertCoupon(Coupon cp);
	public List<Coupon> selectAllCoupon();
	public Coupon selectCoupon(int couponNum);
	public int insertMyCoupon(MyCoupon myCoupon);
	public List<MyCoupon> selectAllMyCoupon(int memberNum);
	public MyCoupon useOneMyCoupon(int myCouponNum);
	public int deleteOneMyCoupon(int myCouponNum);

	public int insertBanListMember(BannedMember bm);
	public MessageReport selectOneReport(int reportNum);

	public List<Member> selectAllBannedList(); 

	public int banMember(String memberId);
	public int unBanMember(String memberId);
	public int deleteBanMember(String memberId);
	
}
