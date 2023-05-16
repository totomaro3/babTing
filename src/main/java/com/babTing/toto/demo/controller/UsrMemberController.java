package com.babTing.toto.demo.controller;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

	@RequestMapping("/usr/member/join")
	public String join() {
		return "usr/member/join";
	}
	
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
	
	@RequestMapping("/usr/member/doCheckData")
	@ResponseBody
	public ResultData doCheckData(double longitude ,double latitude, String name) {
		
		Object[] dataArray = new Object[3];
		dataArray[0] = longitude;
		dataArray[1] = latitude;
		dataArray[2] = name;
		
		return ResultData.from("S-1", "성공적으로 불러왔습니다.","dataArray",dataArray);
	}
	
	@RequestMapping("/usr/member/doJoin")
	@ResponseBody
	public String doJoin(String loginId, String loginPw, String name, String nickname, String cellphoneNum,
			String email, double longitude, double latitude,
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

		ResultData<Integer> joinRd = memberService.join(loginId, Ut.sha256(loginPw), name, nickname, cellphoneNum, email, longitude, latitude);

		if (joinRd.isFail()) {
			return rq.jsHistoryBack(joinRd.getResultCode(), joinRd.getMsg());
		}

		ResultData<Member> getMemberByIdRd = memberService.getMemberById(joinRd.getData1());

		String afterJoinUri = "../member/login?afterLoginUri=" + Ut.getEncodedUri(afterLoginUri);

		return Ut.jsReplace("S-1", Ut.f("회원가입이 완료되었습니다"), afterJoinUri);
	}

	@RequestMapping("/usr/member/login")
	public String login(String loginId, String loginPw, String replaceUri) {
		
		return "usr/member/login";
	}

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

	@RequestMapping("/usr/member/doLogout")
	@ResponseBody
	public String doLogout(String afterLogoutUri) {
		
		rq.logout();
		
		return Ut.jsReplace("S-1", Ut.f("로그아웃 되었습니다."), afterLogoutUri);
	}
	
	@RequestMapping("/usr/member/myPage")
	public String showMyPage() {
		
		return "usr/member/myPage";
	}
	
	@RequestMapping("/usr/member/checkPw")
	public String showCheckPw() {
		
		return "usr/member/checkPw";
	}
	
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
	
	@RequestMapping("/usr/member/modify")
	public String modify() {
		return "/usr/member/modify";
	}

	@RequestMapping("/usr/member/doModify")
	@ResponseBody
	public String doModify(int id, String loginId, String loginPw, String name, String nickname,
			String cellphoneNum, String email, double longitude, double latitude) {
		
		if (Ut.empty(loginPw)) {
			
		} else {
			loginPw = Ut.sha256(loginPw);
		}

		ResultData<String> doModifyMemberRd = memberService.doModifyMember(id, loginPw, name, nickname, cellphoneNum, email, longitude, latitude);
		
		ResultData<Member> loginRd = memberService.login(loginId, loginPw);
		
		rq.login(loginRd.getData1());

		return rq.jsReplace(doModifyMemberRd.getResultCode(), doModifyMemberRd.getMsg(), "../member/myPage");
		
	}

	@RequestMapping("/usr/member/doDelete")
	@ResponseBody
	public String doDelete(int id) {

		memberService.doDeleteMember(id);
		
		return rq.jsReplace("S-1", "회원이 삭제되었습니다.", "../home/main");
		
	}
	
	@RequestMapping("/usr/member/findLoginId")
	public String showFindLoginId() {
		
		return "usr/member/findLoginId";
	}
	
	@RequestMapping("/usr/member/doFindLoginId")
	@ResponseBody
	public String doFindLoginId(String afterFindLoginIdUri, String name, String email) {

		ResultData<String> getMemberByNameAndEmailRd = memberService.getMemberByNameAndEmail(name, email);
				
		return rq.jsReplace(getMemberByNameAndEmailRd.getResultCode(), getMemberByNameAndEmailRd.getMsg(), afterFindLoginIdUri);
	}
	
	@RequestMapping("/usr/member/findLoginPw")
	public String showFindLoginPw() {
		
		return "usr/member/findLoginPw";
	}

	@RequestMapping("/usr/member/doFindLoginPw")
	@ResponseBody
	public String doFindLoginPw(@RequestParam(defaultValue = "/") String afterFindLoginPwUri, String loginId,
			String email) {

		ResultData<Member> getMemberByLoginIdRd = memberService.getMemberByLoginId(loginId);

		if (getMemberByLoginIdRd.getData1() == null) {
			return Ut.jsHistoryBack("F-1", "너는 없는 사람이야");
		}

		if (getMemberByLoginIdRd.getData1().getEmail().equals(email) == false) {
			return Ut.jsHistoryBack("F-2", "일치하는 이메일이 없는데?");
		}

		ResultData notifyTempLoginPwByEmailRd = memberService.notifyTempLoginPwByEmail(getMemberByLoginIdRd.getData1());

		return Ut.jsReplace(notifyTempLoginPwByEmailRd.getResultCode(), notifyTempLoginPwByEmailRd.getMsg(),
				afterFindLoginPwUri);
	}
}

//http://localhost:8081/usr/member/doJoin?loginId=1&loginPw=1&name=abc&nickname=toto&cellphoneNum=1&email=abc@gmail.com
//ResultData -> 표준 보고서 양식
//성공, 실패 쉽게 판단이 가능하도록 / 관련 데이터를 같이 주고 받을 수 있도록