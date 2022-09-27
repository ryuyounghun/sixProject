package com.abc.dao;

import org.apache.ibatis.annotations.Mapper;

import com.abc.domain.Member;

@Mapper
public interface MemberDAO {
	
	// 회원가입하기
	public int insertMember(Member member);
	// 아이디로 회원 부르기
	public Member selectOneMember(String memberId);
	// 주소 업데이트
	public int updateAddressAndPhone(Member member);
	// 포인트 업데이트
	public int updatePoint(Member member);
	// checkID
	public int checkId(String memberId);
	//회원정보 수정하기
	public int updateMember(Member member);
	// 글 쓸 때 경험치 추가하기
	public int addExp(String memberId);
	// 댓글 쓰면 경치 추가
	public int addExpByReply(String memberId);

}
