package com.abc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.abc.dao.MemberDAO;
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


}
