package com.babTing.toto.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.babTing.toto.demo.repository.MemberRepository;
import com.babTing.toto.demo.util.Ut;
import com.babTing.toto.demo.vo.Member;
import com.babTing.toto.demo.vo.ResultData;

@Service
public class MemberService {
	
	@Autowired
	private MemberRepository memberRepository;

	public ResultData<Integer> join(String loginId, String loginPw, String name, String nickname, String cellphoneNum, String email) {
		
		if(memberRepository.isDupLoginId(loginId)) {
			return ResultData.from("F-7", Ut.f("이미 사용중인 아이디(%s)입니다", loginId));
		}
		
		if(memberRepository.isDupNameAndEmail(name,email)) {
			return ResultData.from("F-8", Ut.f("이미 사용중인 이름(%s)과 이메일(%s)입니다", name, email));
		}
		
		memberRepository.doJoinMember(loginId, loginPw, name, nickname, cellphoneNum, email);
		
		int id =  memberRepository.getLastInsertId();
		
		return ResultData.from("S-1", nickname+"회원이 가입되었습니다.","id", id);
		
	}

	public Member getMemberById(int id) {
		
		return memberRepository.getMemberById(id);
	}

	public Member login(String loginId, String loginPw) {

		return memberRepository.getMemberByLoginId(loginId);
	}
}
