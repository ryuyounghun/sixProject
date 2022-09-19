package com.abc.service;

import com.abc.domain.Member;

public interface MemberService {

	// 등록
	public int insertMember(Member member);
	
	// 데이터 불러오기
	public Member selectOneMember(String memberId);
	
	// 업데이트
	public int updateMember(Member member);
	public int updatePoint(Member member);
	public int updateAddressAndPhone(Member member);
	
	// checkID
	public int checkId(String memberId);

	public int useMyPoint(Member member);
}
