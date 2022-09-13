package com.abc.service;

import com.abc.domain.Member;

public interface MemberService {

	public int insertMember(Member member);
	public Member selectOneMember(String memberId);
	public int updateAddressAndPhone(Member member);
}
