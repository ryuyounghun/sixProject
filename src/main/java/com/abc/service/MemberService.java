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
	
	// 일반 글 경험치 추가 메서드
	public int addExp(String memberId);

	// 댓글 쓰면 경험치 추가 메서드
	public int addExpByReply(String memberId);

}
