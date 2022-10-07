package com.abc.domain;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
// 내가 갖고 있는 쿠폰
public class MyCoupon {

	private int myCouponNum;
	private int memberNum;
	private int couponNum;
	private String couponName;
	private int couponPoint;
}
