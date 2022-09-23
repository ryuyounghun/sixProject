package com.abc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.abc.domain.GuestBook;
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
	
	public int useMyPoint(Member member);

	//마이페이지 댓글 달기
	public int insertMyPage(GuestBook guestbook);
	
	// 마이페이지 댓글 하나 보기
	public int selectOneReply(GuestBook guestbook);
	
	// 마이페이지 댓글 전체 보기
	public List<GuestBook> selectAllReply(int memberNum);
	
	// 0923 추가 멤버넘으로 멤버찾기
	public Member selectOneMemberByMemberNum(int memberNum);

}
