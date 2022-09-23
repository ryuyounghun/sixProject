package com.abc.service;

import java.util.List;

import com.abc.domain.GuestBook;
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

	// 마이페이지 댓글작성
	public int insertMyPage(GuestBook guestbook);
	
	// 마이페이지 댓글 하나 보기
	public int selectOneReply(GuestBook guestbook);
	
	// 마이페이지 댓글 전체 보기
	public List<GuestBook> selectAllReply(int memberNum);
	
	// 0923 추가 멤버넘으로 멤버찾기
	public Member selectOneMemberByMemberNum(int memberNum);
	
}
