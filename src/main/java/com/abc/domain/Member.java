package com.abc.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
	private int memberPoint;	// 포인트
	private String roomId;
	
	// 레벨용 추가
	private int memberLevel;	
	
	// 0928 마이페이지 이미지용 추가
	private String originalFile;	 
	private String savedFile;
	
	
	
	// Spring Security를 위한 필드 지정
	private boolean enabled;	// 계정 상태가 유효한지 확인하기 위한 변수 1 이면 유효, 0 이면 유효X
	private String roleName;	// 사용자 계정 권한을 구분, ROLE_USER와 ROLE_ADMIN이 있음
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}
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
