package com.abc.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Member implements UserDetails {

	private int memberNum;
	private String memberId;
	private String memberPw;
	private String memberName;
	private String nickname;
	private String pinNumber;	// 핀번호
	private String address;
	private String phone;
	private int memberExp;		// 경험치
	// 레벨용 추가
	private int memberLevel;		// 레벨
	private int memberPoint;	// 포인트
	
	// Spring Security를 위한 필드 지정
	private boolean enabled;	// 계정 상태가 유효한지 확인하기 위한 변수 1 이면 유효, 0 이면 유효X
	private String roleName;	// 사용자 계정 권한을 구분, ROLE_USER와 ROLE_ADMIN이 있음
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/**
	public int calcLevel(int memberExp) {
		int exp = this.memberExp;
		int level = 0;
		if ( exp <= 1000) {
			level = 1;
		}
		else if ( exp <= 2000) {
			level = 2;
		}
		else if ( exp <= 5000) {
			level = 3;
		}
		else if ( exp <= 10000) {
			level = 4;
		}
		else if ( exp <= 15000) {
			level = 5;
		}
		else if ( exp <= 16000) {
			level = 6;
		}
		else if ( exp <= 17000) {
			level = 7;
		}
		else if ( exp <= 18000) {
			level = 8;
		}
		else if ( exp <= 19000) {
			level = 9;
		}
		else if ( exp <= 20000) {
			level = 10;
		}
		return this.memberLevel = level;
	}
	*/
	
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.memberPw;
	}
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.memberId;
	}
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
}
