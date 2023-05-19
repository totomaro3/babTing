package com.babTing.toto.demo.vo;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.babTing.toto.demo.util.Ut;

import lombok.Getter;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Rq {
	@Getter
	private boolean isLogined;
	@Getter
	private int loginedMemberId;
	@Getter
	private String loginedMemberNickname;
	@Getter
	private Member loginedMember;
	
	HttpServletRequest req;
	HttpServletResponse resp;
	HttpSession session;
	Map<String, String> paramMap;
	
	/**
	 * 로그인 시 초기 설정
	 * @param req
	 * @param resp
	 */
	public Rq(HttpServletRequest req,HttpServletResponse resp) {
		this.req = req;
		this.resp = resp;
		this.session = req.getSession();
		this.paramMap = Ut.getParamMap(req);
		
		boolean isLogined = false;
		int loginedMemberId = 0;
		String loginedMemberNickname = "";
		Member loginedMember = null;
		

		if (session.getAttribute("loginedMember") != null) {
			isLogined = true;
			loginedMemberId = (int) session.getAttribute("loginedMemberId");
			loginedMemberNickname = (String) session.getAttribute("loginedMemberNickname");
			loginedMember = (Member) session.getAttribute("loginedMember");
		}

		this.isLogined = isLogined;
		this.loginedMemberId = loginedMemberId;
		this.loginedMemberNickname = loginedMemberNickname;
		this.loginedMember = loginedMember;
		
		this.req.setAttribute("rq", this);

	}
	
	/**
	 * 알림 후 뒤로가기 실행
	 * @param resultCode
	 * @param msg
	 * @return
	 */
	public String jsHistoryBack(String resultCode, String msg) {
		return Ut.jsHistoryBack(resultCode, msg);
	}

	/**
	 * 알림 후 새로고침 코드x
	 * @param msg
	 * @param uri
	 * @return
	 */
	public String jsReplace(String msg, String uri) {
		return Ut.jsReplace(msg, uri);
	}
	
	/**
	 * 알림 후 새로고침
	 * @param resultCode
	 * @param msg
	 * @param uri
	 * @return
	 */
	public String jsReplace(String resultCode ,String msg, String uri) {
		return Ut.jsReplace(resultCode, msg, uri);
	}

	/**
	 * 
	 * @param str
	 * @throws IOException
	 */
	public void printJs(String str) throws IOException {
		resp.setContentType("text/html; charset=UTF-8");
		resp.getWriter().append(str);
	}
	
	
	/**
	 * show관련 controller의 에러 처리
	 * @param msg
	 * @return
	 */
	public String jsHitoryBackOnView(String msg) {
		req.setAttribute("msg", msg);
		req.setAttribute("historyBack", true);
		return "usr/common/js";
	}
	
	/**
	 * 로그인시 세션 추가
	 * @param member
	 */
	public void login(Member member) {
		session.setAttribute("loginedMemberId", member.getId());
		session.setAttribute("loginedMemberNickname", member.getNickname());
		session.setAttribute("loginedMember", member);
	}
	
	/**
	 * 로그아웃시 세션 제거
	 */
	public void logout() {
		session.removeAttribute("loginedMemberId");
		session.removeAttribute("loginedMemberNickname");
		session.removeAttribute("loginedMember");
	}
	
	/**
	 * 로그인 할 때 마지막으로 방문한 페이지 찾기
	 */
	
	public String getCurrentUri() {
		String currentUri = req.getRequestURI();
		String queryString = req.getQueryString();
		
		currentUri += "?" + queryString;
		
		return currentUri;
	}
	
	public String getEncodedCurrentUri() {
		return Ut.getEncodedCurrentUri(getCurrentUri());
	}
	
	public String getLoginUri() {
		return "../member/login?afterLoginUri=" + getAfterLoginUri();
	}

	private String getAfterLoginUri() {
//		로그인 후 접근 불가 페이지

		String requestUri = req.getRequestURI();

		switch (requestUri) {
		case "/usr/member/login":
		case "/usr/member/join":
			return Ut.getEncodedUri(Ut.getAttr(paramMap, "afterLoginUri", ""));

		}

		return getEncodedCurrentUri();
	}
	
	public String getLogoutUri() {
		String requestUri = req.getRequestURI();

		switch (requestUri) {
		case "/usr/article/write":
			return "../member/doLogout?afterLogoutUri=" + "/";
		}

		return "../member/doLogout?afterLogoutUri=" + getAfterLogoutUri();
	}

	public String getAfterLogoutUri() {
		return getEncodedCurrentUri();
	}
	
	public String getFindLoginIdUri() {
		return "../member/findLoginId?afterFindLoginIdUri=" + getAfterFindLoginIdUri();
	}

	private String getAfterFindLoginIdUri() {
		return getEncodedCurrentUri();
	}

	public String getFindLoginPwUri() {
		return "../member/findLoginPw?afterFindLoginPwUri=" + getAfterFindLoginPwUri();
	}

	private String getAfterFindLoginPwUri() {
		return getEncodedCurrentUri();
	}
	
	/**
	 * 인터셉터 초기 가동 용, 삭제 금지
	 */
	public void initOnBeforeActionInterceptor() {

	}

}