package com.babTing.toto.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.babTing.toto.demo.repository.MemberRepository;
import com.babTing.toto.demo.util.Ut;
import com.babTing.toto.demo.vo.Member;
import com.babTing.toto.demo.vo.ResultData;

@Service
public class MemberService {
	
	@Value("${custom.siteMainUri}")
	private String siteMainUri;
	@Value("${custom.siteName}")
	private String siteName;
	
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private MailService mailService;

	public ResultData<Integer> join(String loginId, String loginPw, String name, String nickname, String cellphoneNum, String email, double longitude, double latitude) {
		
		if(memberRepository.isDupLoginId(loginId)) {
			return ResultData.from("F-7", Ut.f("이미 사용중인 아이디(%s)입니다", loginId));
		}
		
		if(memberRepository.isDupNameAndEmail(name,email)) {
			return ResultData.from("F-8", Ut.f("이미 사용중인 이름(%s)과 이메일(%s)입니다", name, email));
		}
		
		memberRepository.doJoinMember(loginId, loginPw, name, nickname, cellphoneNum, email, longitude, latitude);
		
		int id =  memberRepository.getLastInsertId();
		
		return ResultData.from("S-1", nickname+"회원이 가입되었습니다.","id", id);
		
	}

	public ResultData<Member> getMemberById(int id) {
		
		Member member = memberRepository.getMemberById(id);
		
		return ResultData.from("S-1", "멤버를 찾았습니다.","member", member);
	}

	public ResultData<Member> login(String loginId, String loginPw) {
		
		Member member = memberRepository.getMemberByLoginId(loginId);
		
		return ResultData.from("S-1", Ut.f("%s님 환영합니다.", member.getNickname()),"member", member);
	}


	public ResultData<String> doModifyMember(int id, String loginPw, String name, String nickname, String cellphoneNum, String email, double longitude, double latitude) {
		
		memberRepository.doModifyMember(id, loginPw, name, nickname, cellphoneNum, email, longitude, latitude);
		
		return ResultData.from("S-1", nickname+"회원이 수정되었습니다.","nickname", nickname);
	}

	public ResultData<Integer> doDeleteMember(int id) {
		memberRepository.doDeleteMember(id);
		
		return ResultData.from("S-1", "회원이 삭제되었습니다.","id", id);
	}

public ResultData<Boolean> isDupLoginId(String loginId) {
		
		if(memberRepository.isDupLoginId(loginId)) {
			return ResultData.from("F-2", "중복된 아이디 입니다.","isDupLoginId", true);
		}
		else {
			return ResultData.from("S-1", "사용 가능한 아이디 입니다.","isDupLoginId", false);
		}
	}
	
	public ResultData<Member> getMemberByLoginId(String loginId) {
		
		Member member = memberRepository.getMemberByLoginId(loginId);
		
		return ResultData.from("S-1", "멤버를 찾았습니다.","Member", member);
	}

	public ResultData<String> getMemberByNameAndEmail(String name, String email) {
		
		Member member = memberRepository.getMemberByNameAndEmail(name, email);
		
		if(member == null) {
			return ResultData.from("F-1", "찾은 아이디가 없습니다.");
		}
		
		return ResultData.from("S-1", "찾은 아이디는 "+member.getLoginId()+"입니다", "loginId", member.getLoginId());
	}
	
	public ResultData notifyTempLoginPwByEmail(Member actor) {
		String title = "[" + siteName + "] 임시 패스워드 발송";
		String tempPassword = Ut.getTempPassword(6);
		String body = "<h1>임시 패스워드 : " + tempPassword + "</h1>";
		body += "<a href=\"" + siteMainUri + "/usr/member/login\" target=\"_blank\">로그인 하러가기</a>";

		ResultData sendResultData = mailService.send(actor.getEmail(), title, body);

		if (sendResultData.isFail()) {
			return sendResultData;
		}

		setTempPassword(actor, tempPassword);

		return ResultData.from("S-1", "계정의 이메일주소로 임시 패스워드가 발송되었습니다.");
	}

	private void setTempPassword(Member actor, String tempPassword) {
		memberRepository.doModifyMember(actor.getId(), Ut.sha256(tempPassword), null, null, null, null, 0, 0);
	}
	
	
}
