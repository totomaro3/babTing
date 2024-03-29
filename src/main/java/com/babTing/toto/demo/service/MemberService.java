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

	/**
	 * 회원 가입
	 * @param loginId
	 * @param loginPw
	 * @param name
	 * @param nickname
	 * @param cellphoneNum
	 * @param email
	 * @param longitude
	 * @param latitude
	 * @return
	 */
	public ResultData<Integer> join(String loginId, String loginPw, String name, String nickname, String cellphoneNum, String email, String addressName, String address, double longitude, double latitude) {
		
		if(memberRepository.isDupLoginId(loginId)) {
			return ResultData.from("F-7", Ut.f("이미 사용중인 아이디(%s)입니다", loginId));
		}
		
		if(memberRepository.isDupNameAndEmail(name,email)) {
			return ResultData.from("F-8", Ut.f("이미 사용중인 이름(%s)과 이메일(%s)입니다", name, email));
		}
		
		memberRepository.doJoinMember(loginId, loginPw, name, nickname, cellphoneNum, email, addressName, address, longitude, latitude);
		
		int id =  memberRepository.getLastInsertId();
		
		return ResultData.from("S-1", nickname+"회원이 가입되었습니다.","id", id);
		
	}

	/**
	 * ID로 회원 찾기
	 * @param id
	 * @return
	 */
	public ResultData<Member> getMemberById(int id) {
		
		Member member = memberRepository.getMemberById(id);
		
		return ResultData.from("S-1", "멤버를 찾았습니다.","member", member);
	}

	/**
	 * 로그인
	 * @param loginId
	 * @param loginPw
	 * @return
	 */
	public ResultData<Member> login(String loginId, String loginPw) {
		
		Member member = memberRepository.getMemberByLoginId(loginId);
		
		return ResultData.from("S-1", Ut.f("%s님 환영합니다.", member.getNickname()),"member", member);
	}

	/**
	 * 회원 수정
	 * @param id
	 * @param loginPw
	 * @param name
	 * @param nickname
	 * @param cellphoneNum
	 * @param email
	 * @param longitude
	 * @param latitude
	 * @return
	 */
	public ResultData<String> doModifyMember(int id, String loginPw, String name, String nickname, String cellphoneNum, String email,String addressName, String address, double longitude, double latitude) {
		
		memberRepository.doModifyMember(id, loginPw, name, nickname, cellphoneNum, email, addressName, address, longitude, latitude);
		
		return ResultData.from("S-1", nickname+"회원이 수정되었습니다.","nickname", nickname);
	}

	/**
	 * 회원 삭제
	 * @param id
	 * @return
	 */
	public ResultData<Integer> doDeleteMember(int id) {
		memberRepository.doDeleteMember(id);
		
		return ResultData.from("S-1", "회원이 삭제되었습니다.","id", id);
	}

	/**
	 * 중복 아이디 체크
	 * @param loginId
	 * @return
	 */
	public ResultData<Boolean> isDupLoginId(String loginId) {
		
		if(memberRepository.isDupLoginId(loginId)) {
			return ResultData.from("F-2", "중복된 아이디 입니다.","isDupLoginId", true);
		}
		else {
			return ResultData.from("S-1", "사용 가능한 아이디 입니다.","isDupLoginId", false);
		}
	}
	
	/**
	 * loginId로 회원 찾기
	 * @param loginId
	 * @return
	 */
	public ResultData<Member> getMemberByLoginId(String loginId) {
		
		Member member = memberRepository.getMemberByLoginId(loginId);
		
		return ResultData.from("S-1", "멤버를 찾았습니다.","Member", member);
	}

	/**
	 * 이름과 이메일로 회원 찾기
	 * @param name
	 * @param email
	 * @return
	 */
	public ResultData<String> getMemberByNameAndEmail(String name, String email) {
		
		Member member = memberRepository.getMemberByNameAndEmail(name, email);
		
		if(member == null) {
			return ResultData.from("F-1", "찾은 아이디가 없습니다.");
		}
		
		return ResultData.from("S-1", "찾은 아이디는 "+member.getLoginId()+"입니다", "loginId", member.getLoginId());
	}
	
	/**
	 * 비밀번호 찾기 중 임시 비밀번호 이메일 발송
	 * @param actor
	 * @return
	 */
	public ResultData notifyTempLoginPwByEmail(Member actor) {
		String title = "[" + siteName + "] 임시 패스워드 발송";
		String tempPassword = Ut.getTempPassword(6);
		String body = "<h1>임시 패스워드 : " + tempPassword + "</h1>";
		body += "<div>로그인 후에 비밀번호를 꼭 변경하시길 바랍니다!</div>";
		body += "<a href=\"" + siteMainUri + "/usr/member/login?afterLoginUri=%2Fusr%2Fhome%2Fmain%3Fnull\" target=\"_blank\">로그인 하러가기</a>";

		ResultData sendResultData = mailService.send(actor.getEmail(), title, body);

		if (sendResultData.isFail()) {
			return sendResultData;
		}

		setTempPassword(actor, tempPassword);

		return ResultData.from("S-1", "계정의 이메일주소로 임시 패스워드가 발송되었습니다.");
	}

	/**
	 * 비밀번호 찾기 중 임시 비밀번호 부여
	 * @param actor
	 * @param tempPassword
	 */
	private void setTempPassword(Member actor, String tempPassword) {
		memberRepository.doModifyMember(actor.getId(), Ut.sha256(tempPassword), "", "", "", "", "", "", 0, 0);
	}

	public void modifyKeyword(int id, String keyword1, String keyword2, String keyword3, String keyword4, String keyword5) {
		memberRepository.doModifyKeyword(id, keyword1, keyword2, keyword3, keyword4, keyword5);
	}

	public ResultData<Boolean> isDupEmail(String email) {
		if(memberRepository.isDupEmail(email)) {
			return ResultData.from("F-2", "중복된 이메일 입니다.","isDupEmail", true);
		}
		else {
			return ResultData.from("S-1", "사용 가능한 이메일 입니다.","isDupEmail", false);
		}
	}
}
