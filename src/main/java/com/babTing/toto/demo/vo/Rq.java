package com.babTing.toto.demo.vo;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Rq {
	@Getter
	private boolean isLogined;
	@Getter
	private int loginedMemberId;
	
	HttpServletRequest req;
	HttpServletResponse resp;
	HttpSession session;
	
	public Rq(HttpServletRequest req,HttpServletResponse resp) {
		this.req = req;
		this.resp = resp;
		this.session = req.getSession();
		
		boolean isLogined = false;
		int loginedMemberId = 0;

		if (session.getAttribute("loginedMemberId") != null) {
			isLogined = true;
			loginedMemberId = (int) session.getAttribute("loginedMemberId");
		}

		this.isLogined = isLogined;
		this.loginedMemberId = loginedMemberId;
		
		this.req.setAttribute("rq", this);

	}

	public void printHistoryBackJs(String str) throws IOException {
		resp.getWriter().append(str);
	}
	
	public void returnHistoryBackJs(String str) throws IOException {
		resp.getWriter().append(str);
	}
	
	public void login(Member member) {
		session.setAttribute("loginedMemberId", member.getId());
		session.setAttribute("loginedMemberNickname", member.getNickname());
		session.setAttribute("loginedMemberAuthLevel", member.getAuthLevel());
	}
	
	public void logout() {
		session.removeAttribute("loginedMemberId");
	}
	
	public void initOnBeforeActionInterceptor() {

	}

}