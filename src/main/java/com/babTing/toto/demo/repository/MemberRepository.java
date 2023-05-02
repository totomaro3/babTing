package com.babTing.toto.demo.repository;

import org.apache.ibatis.annotations.Mapper;

import com.babTing.toto.demo.vo.Member;

@Mapper
public interface MemberRepository {

	public void doJoinMember(String loginId, String loginPw, String name, String nickname, String cellphoneNum, String email);

	public Member getMemberById(int id);

	public boolean isDupLoginId(String loginId);

	public int getLastInsertId();

	public boolean isDupNameAndEmail(String name, String email);

	public Member getMemberByLoginId(String loginId);
}