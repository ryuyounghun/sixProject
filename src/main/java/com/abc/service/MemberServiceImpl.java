package com.abc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.abc.dao.MemberDAO;
import com.abc.domain.GuestBook;
import com.abc.domain.Member;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService {
	
	// 데이터베이스를 컨트롤하기 위한 MemberDAO 객체
	@Autowired
	private MemberDAO mDao;
	
	// SecurityConfig에 있는 비밀번호를 암호화하기 위한 객체
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public int insertMember(Member member) {
		
		log.debug("insertMember() 실행");
		log.debug("Member 객체 : {}", member);
		
		// Member 객체에 평문으로 들어있는 비번을 암호화 한다
		String encodedPassword = passwordEncoder.encode(member.getPassword());
		
		// 암호화된 비번을 다시 Member객체에 설정
		member.setMemberPw(encodedPassword);
		
		return mDao.insertMember(member);
	}

	@Override
	public Member selectOneMember(String memberId) {
		
		log.debug("selectOneMember() 실행");
		log.debug("id : {}", memberId);
		
		return mDao.selectOneMember(memberId);
	}

	@Override
	public int updateAddressAndPhone(Member member) {
		return mDao.updateAddressAndPhone(member);
	}

	@Override
	public int updatePoint(Member member) {
		return mDao.updatePoint(member);
	}

	@Override
	public int updateMember(Member member) {
		String pw = member.getMemberPw();
		
		String encodePw = passwordEncoder.encode(pw);
		
		member.setMemberPw(encodePw); 
		
		return mDao.updateMember(member);
	}

	@Override
	public int checkId(String memberId) {
		int cnt = mDao.checkId(memberId);
		
		log.debug("memberId : {}", memberId);
		
		return cnt;
	}

	@Override
	public int useMyPoint(Member member) {
		// TODO Auto-generated method stub
		return mDao.useMyPoint(member);
	}

	@Override
	public int insertMyPage(GuestBook guestbook) {
		// TODO Auto-generated method stub
		return mDao.insertMyPage(guestbook);
	}

	@Override
	public int selectOneReply(GuestBook guestbook) {
		// TODO Auto-generated method stub
		return mDao.selectOneReply(guestbook);
	}

	@Override
	public List<GuestBook> selectAllReply(int memberNum) {
		// TODO Auto-generated method stub
		return mDao.selectAllReply(memberNum);
	}

	@Override
	public Member selectOneMemberByMemberNum(int memberNum) {
		// TODO Auto-generated method stub
		return mDao.selectOneMemberByMemberNum(memberNum);
	}

	@Override
	public int setRoomId(Member member) {
		// TODO Auto-generated method stub
		return mDao.setRoomId(member);
	}

	@Override
	public int blockLogin(Member member) {
		// TODO Auto-generated method stub
		return mDao.blockLogin(member);
	}

	@Override
	public int checkPnum(String pinNumber) {
		// TODO Auto-generated method stub
		return mDao.checkPnum(pinNumber);
	}

	@Override
	public String findMemberId(String pinNumber) {
		// TODO Auto-generated method stub
		return mDao.findMemberId(pinNumber);
	}

	@Override
	   public int updatePw(Member member) {
	      // TODO Auto-generated method stub
	      // 1002 세련 수정함
	      String encodePw = passwordEncoder.encode(member.getMemberPw());
	      
	      member.setMemberPw(encodePw); 
	      
	      return mDao.updatePw(member);
	   }

	@Override
	public int addExp(String memberId) {
		log.debug("경험치 대상 : {}", memberId);
		return mDao.addExp(memberId);
	}

	
	@Override
	public int addExpByReply(String memberId) {
		log.debug("경험치 대상 : {}", memberId);
		return mDao.addExpByReply(memberId);
	}

	@Override
	public int updateMyImage(Member member) {
		// TODO Auto-generated method stub
		return mDao.updateMyImage(member);
	}

	@Override
	public GuestBook selectGuestBookByNum(int guestBookNum) {
		// TODO Auto-generated method stub
		return mDao.selectGuestBookByNum(guestBookNum);
	}

	@Override
	public int updateGuestBook(GuestBook guestBook) {
		// TODO Auto-generated method stub
		return mDao.updateGuestBook(guestBook);
	}

	@Override
	public int deleteGuestBook(int guestBookNum) {
		// TODO Auto-generated method stub
		return mDao.deleteGuestBook(guestBookNum);
	}
}
