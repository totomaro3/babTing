package com.babTing.toto.demo.controller;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.babTing.toto.demo.service.MemberService;
import com.babTing.toto.demo.util.Ut;
import com.babTing.toto.demo.vo.Member;
import com.babTing.toto.demo.vo.ResultData;
import com.babTing.toto.demo.vo.Rq;

@Controller
public class UsrMemberController {

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private Rq rq;
	
	/**
	 * 회원 가입 폼 join
	 * @return 가입 이동
	 */
	@RequestMapping("/usr/member/join")
	public String showJoin() {
		return "usr/member/join";
	}
	
	/**
	 * 아이디 중복 체크 (가입 시 사용) Ajax getLoginIdDup
	 * @param loginId
	 * @return
	 */
	@RequestMapping("/usr/member/getLoginIdDup")
	@ResponseBody
	public ResultData<String> getLoginIdDup(String loginId) {
		
		if(Ut.empty(loginId)) {
			return ResultData.from("F-1", "아이디를 입력해주세요.","loginId",loginId);
		}
		
		ResultData<Boolean> getMemberByLoginIdRd = memberService.isDupLoginId(loginId);
		
		if(getMemberByLoginIdRd.getData1()) {
			return ResultData.from("F-2", getMemberByLoginIdRd.getMsg(),"loginId",loginId);
		}
		
		return ResultData.from("S-1", getMemberByLoginIdRd.getMsg(),"loginId",loginId);
	}
	
	/**
	 * 비밀번호 일치 체크 (가입 시 사용) Ajax getLoginPwConfirm
	 * @param loginPw
	 * @param loginPwConfirm
	 * @return
	 */
	@RequestMapping("/usr/member/getLoginPwConfirm")
	@ResponseBody
	public ResultData<String> getLoginPwConfirm(String loginPw, String loginPwConfirm) {
		
		if (Ut.empty(loginPw)) {
			return ResultData.from("F-1", "비밀번호를 입력해주세요");
		}
		
		if (Ut.empty(loginPw)) {
			return ResultData.from("F-2", "비밀번호확인을 입력해주세요");
		}
		
		if(!loginPw.equals(loginPwConfirm)) {
			return ResultData.from("F-3", "비밀번호가 일치하지 않습니다.");
		}
		
		return ResultData.from("S-1", "비밀번호가 일치합니다.");
	}
	
	@RequestMapping("/usr/member/getEmailDup")
	@ResponseBody
	public ResultData<String> getEmailDup(String email) {
		
		if (Ut.empty(email)) {
			return ResultData.from("F-1", "이메일을 입력해주세요");
		}
		
	    String emailRegex = "\\S+@\\S+\\.\\S+"; // 이메일 유효성 검사용 정규표현식

	    Pattern pattern = Pattern.compile(emailRegex);
	    Matcher matcher = pattern.matcher(email);

	    if (!matcher.matches()) {
	    	return ResultData.from("F-2", "올바른 이메일 형식이 아닙니다.");
	    }
		
		ResultData<Boolean> isDupEmailRd = memberService.isDupEmail(email);
		
		if(isDupEmailRd.getData1()) {
			return ResultData.from("F-2", isDupEmailRd.getMsg(),"email",email);
		}
		
		return ResultData.from("S-1", "사용가능한 이메일 입니다.","email",email);
	}
	
	/**
	 * kakaoMap에서 사용자가 입력한 지도 위치 불러오기 Ajax doCheckData
	 * @param longitude
	 * @param latitude
	 * @param name
	 * @param address
	 * @return
	 */
	@RequestMapping("/usr/member/doCheckData")
	@ResponseBody
	public ResultData doCheckData(double longitude ,double latitude, String name, String address, String category) {
		
		Object[] dataArray = new Object[5];
		dataArray[0] = longitude;
		dataArray[1] = latitude;
		dataArray[2] = name;
		dataArray[3] = address;
		dataArray[4] = category;
		
		return ResultData.from("S-1", "성공적으로 불러왔습니다.","dataArray",dataArray);
	}
	
	/**
	 * 회원 가입 (INSERT) doJoin
	 * @param loginId
	 * @param loginPw
	 * @param name
	 * @param nickname
	 * @param cellphoneNum
	 * @param email
	 * @param longitude
	 * @param latitude
	 * @param afterLoginUri
	 * @return
	 */
	@RequestMapping("/usr/member/doJoin")
	@ResponseBody
	public String doJoin(String loginId, String loginPw, String name, String nickname, String cellphoneNum,
			String email, String addressName, String address, double longitude, double latitude,
			@RequestParam(defaultValue = "/") String afterLoginUri) {

		if (Ut.empty(loginId)) {
			return rq.jsHistoryBack("F-1", "아이디를 입력해주세요");
		}
		if (Ut.empty(loginPw)) {
			return rq.jsHistoryBack("F-2", "비밀번호를 입력해주세요");
		}
		if (Ut.empty(name)) {
			return rq.jsHistoryBack("F-3", "이름을 입력해주세요");
		}
		if (Ut.empty(nickname)) {
			return rq.jsHistoryBack("F-4", "닉네임을 입력해주세요");
		}
		if (Ut.empty(cellphoneNum)) {
			return rq.jsHistoryBack("F-5", "전화번호를 입력해주세요");
		}
		if (Ut.empty(email)) {
			return rq.jsHistoryBack("F-6", "이메일을 입력해주세요");
		}

		ResultData<Integer> joinRd = memberService.join(loginId, Ut.sha256(loginPw), name, nickname, cellphoneNum, email, addressName, address, longitude, latitude);

		if (joinRd.isFail()) {
			return rq.jsHistoryBack(joinRd.getResultCode(), joinRd.getMsg());
		}

		ResultData<Member> getMemberByIdRd = memberService.getMemberById(joinRd.getData1());

		String afterJoinUri = "../member/login?afterLoginUri=" + Ut.getEncodedUri(afterLoginUri);

		return Ut.jsReplace("S-1", Ut.f("회원가입이 완료되었습니다"), afterJoinUri);
	}

	/**
	 * 로그인 폼 login
	 * @param loginId
	 * @param loginPw
	 * @param replaceUri
	 * @return 로그인 이동
	 */
	@RequestMapping("/usr/member/login")
	public String showLogin(String loginId, String loginPw, String replaceUri) {
		
		return "usr/member/login";
	}

	/**
	 * 로그인 doLogin
	 * @param loginId
	 * @param loginPw
	 * @param afterLoginUri
	 * @return 로그인 알림
	 */
	@RequestMapping("/usr/member/doLogin")
	@ResponseBody
	public String doLogin(String loginId, String loginPw, String afterLoginUri) {

		if (Ut.empty(loginId)) {
			return Ut.jsHistoryBack("F-1", "아이디를 입력해주세요");
		}
		if (Ut.empty(loginPw)) {
			return Ut.jsHistoryBack("F-2", "비밀번호를 입력해주세요");
		}

		ResultData<Member> loginRd = memberService.login(loginId, Ut.sha256(loginPw));

		if (loginRd.getData1() == null) {
			return Ut.jsHistoryBack("F-3", Ut.f("아이디(%s)가 없습니다.", loginId));
		}

		if (!loginRd.getData1().getLoginPw().equals(Ut.sha256(loginPw))) {
			return Ut.jsHistoryBack("F-4", Ut.f("비밀번호가 일치하지 않습니다."));
		}
		
		rq.login(loginRd.getData1());
		
		return Ut.jsReplace("S-1", Ut.f("%s님 환영합니다.", loginRd.getData1().getNickname()), afterLoginUri);
	}

	/**
	 * 로그아웃 doLogout
	 * @param afterLogoutUri
	 * @return 로그아웃 알림
	 */
	@RequestMapping("/usr/member/doLogout")
	@ResponseBody
	public String doLogout(String afterLogoutUri) {
		
		rq.logout();
		
		return Ut.jsReplace("S-1", Ut.f("로그아웃 되었습니다."), afterLogoutUri);
	}
	
	/**
	 * 내 정보 myPage
	 * @return 이동
	 */
	@RequestMapping("/usr/member/myPage")
	public String showMyPage() {
		
		return "usr/member/myPage";
	}
	
	/**
	 * 비밀번호 본인 확인 폼 (회원 정보 수정 시 사용) checkPw
	 * @return
	 */
	@RequestMapping("/usr/member/checkPw")
	public String showCheckPw() {
		
		return "usr/member/checkPw";
	}
	
	/**
	 * 비밀번호 본인 확인 (회원 정보 수정 시 사용) doCheckPw
	 * @param loginId
	 * @param loginPw
	 * @return
	 */
	@RequestMapping("/usr/member/doCheckPw")
	@ResponseBody
	public String doCheckPw(String loginId, String loginPw) {
		
		if (Ut.empty(loginPw)) {
			return rq.jsHistoryBack("F-1","비밀번호를 입력해주세요");
		}

		ResultData<Member> getMemberByLoginId = memberService.getMemberByLoginId(loginId);

		if (!getMemberByLoginId.getData1().getLoginPw().equals(Ut.sha256(loginPw))) {
			return rq.jsHistoryBack("F-1","비밀번호가 일치하지 않습니다.");
		}
		
		return rq.jsReplace("", "modify");
	}
	
	/**
	 * 회원 정보 수정 폼 modify
	 * @return
	 */
	@RequestMapping("/usr/member/modify")
	public String showModify() {
		return "/usr/member/modify";
	}

	/**
	 * 회원 정보 수정 (UPDATE) doModify
	 * @param id
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
	@RequestMapping("/usr/member/doModify")
	@ResponseBody
	public String doModify(int id, String loginId, String loginPw, String name, String nickname,
			String cellphoneNum, String email, String addressName, String address, double longitude, double latitude) {
		
		if (Ut.empty(loginPw)) {
			
		} else {
			loginPw = Ut.sha256(loginPw);
		}

		ResultData<String> doModifyMemberRd = memberService.doModifyMember(id, loginPw, name, nickname, cellphoneNum, email, addressName, address, longitude, latitude);
		
		ResultData<Member> loginRd = memberService.login(loginId, loginPw);
		
		rq.login(loginRd.getData1());

		return rq.jsReplace(doModifyMemberRd.getResultCode(), doModifyMemberRd.getMsg(), "../member/myPage");
		
	}

	/**
	 * 회원 정보 삭제 (UPDATE) Delstatus 조정 , 탈퇴 회원 정보 유지 doDelete
	 * @param id
	 * @return
	 */
	@RequestMapping("/usr/member/doDelete")
	@ResponseBody
	public String doDelete(int id) {

		memberService.doDeleteMember(id);
		
		return rq.jsReplace("S-1", "회원이 삭제되었습니다.", "../home/main");
		
	}
	
	/**
	 * 아이디 찾기 폼 findLoginId
	 * @return
	 */
	@RequestMapping("/usr/member/findLoginId")
	public String showFindLoginId() {
		
		return "usr/member/findLoginId";
	}
	
	/**
	 * 아이디 찾기 (이름과 이메일 일치 체크) doFindLoginId
	 * @param afterFindLoginIdUri
	 * @param name
	 * @param email
	 * @return
	 */
	@RequestMapping("/usr/member/doFindLoginId")
	@ResponseBody
	public String doFindLoginId(String afterFindLoginIdUri, String name, String email) {

		ResultData<String> getMemberByNameAndEmailRd = memberService.getMemberByNameAndEmail(name, email);
				
		return rq.jsReplace(getMemberByNameAndEmailRd.getResultCode(), getMemberByNameAndEmailRd.getMsg(), afterFindLoginIdUri);
	}
	
	/**
	 * 비밀번호 찾기 폼 findLoginPw
	 * @return
	 */
	@RequestMapping("/usr/member/findLoginPw")
	public String showFindLoginPw() {
		
		return "usr/member/findLoginPw";
	}

	/**
	 * 비밀번호 찾기 (이름과 이메일 일치 체크) doFindLoginPw
	 * @param afterFindLoginPwUri
	 * @param loginId
	 * @param email
	 * @return
	 */
	@RequestMapping("/usr/member/doFindLoginPw")
	@ResponseBody
	public String doFindLoginPw(@RequestParam(defaultValue = "/") String afterFindLoginPwUri, String loginId,
			String email) {

		ResultData<Member> getMemberByLoginIdRd = memberService.getMemberByLoginId(loginId);

		if (getMemberByLoginIdRd.getData1() == null) {
			return Ut.jsHistoryBack("F-1", "가입되어 있는 이름이 없습니다");
		}

		if (getMemberByLoginIdRd.getData1().getEmail().equals(email) == false) {
			return Ut.jsHistoryBack("F-2", "이메일이 일치하지 않습니다.");
		}

		ResultData notifyTempLoginPwByEmailRd = memberService.notifyTempLoginPwByEmail(getMemberByLoginIdRd.getData1());

		return Ut.jsReplace(notifyTempLoginPwByEmailRd.getResultCode(), notifyTempLoginPwByEmailRd.getMsg(),
				afterFindLoginPwUri);
	}
	
	/**
	 * 키워드 설정 폼 setKeyword
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping("/usr/member/setKeyword")
	public String showSetKeyword(Model model, int id) {

		ResultData<Member> getMemberByIdRd = memberService.getMemberById(id);

		Member member = getMemberByIdRd.getData1();

		if (member == null) {
			return rq.jsHitoryBackOnView("해당 멤버는 없습니다.");
		}

		model.addAttribute("member", member);

		return "usr/member/setKeyword";
	}
	
	/**
	 * 키워드 설정 doSetKeyword
	 * @param id
	 * @param keyword1
	 * @param keyword2
	 * @param keyword3
	 * @param keyword4
	 * @param keyword5
	 * @return
	 */
	@RequestMapping("/usr/member/doSetKeyword")
	@ResponseBody
	public String doSetKeyword(int id, String keyword1, String keyword2, String keyword3, String keyword4, String keyword5) {

		ResultData<Member> getMemberByIdRd = memberService.getMemberById(id);

		Member member = getMemberByIdRd.getData1();

		if (member == null) {
			return rq.jsHitoryBackOnView("해당 멤버는 없습니다.");
		}
		
		memberService.modifyKeyword(id, keyword1, keyword2, keyword3, keyword4, keyword5);
		
		if(keyword1 != "") rq.getLoginedMember().setKeyword1(keyword1);
		if(keyword2 != "") rq.getLoginedMember().setKeyword2(keyword2);
		if(keyword3 != "") rq.getLoginedMember().setKeyword3(keyword3);
		if(keyword4 != "") rq.getLoginedMember().setKeyword4(keyword4);
		if(keyword5 != "") rq.getLoginedMember().setKeyword5(keyword5);

		return rq.jsReplace("S-1", "키워드가 수정되었습니다.", "../home/main");
	}
}