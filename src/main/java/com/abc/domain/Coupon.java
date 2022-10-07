package com.abc.domain;

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
// 구매할 수 있는 쿠폰 리스트
public class Coupon {

	private int couponNum;
	private String couponName;
	private int couponPoint;
}
