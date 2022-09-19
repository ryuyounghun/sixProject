package com.abc.dao;

import org.apache.ibatis.annotations.Mapper;

import com.abc.domain.Member;

@Mapper
public interface MemberDAO {
	
	// 회원가입하기
	public int insertMember(Member member);
	//
	public Member selectOneMember(String memberId);
	
	public int updateAddressAndPhone(Member member);
	
	public int updatePoint(Member member);
	
	public int useMyPoint(Member member);
}
