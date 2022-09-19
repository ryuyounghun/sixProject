package com.abc.service;

import com.abc.domain.Member;

public interface MemberService {

	public int insertMember(Member member);
	public Member selectOneMember(String memberId);
	public int updateAddressAndPhone(Member member);
	public int updatePoint(Member member);
	public int useMyPoint(Member member);
}
