package com.babTing.toto.demo.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.babTing.toto.demo.util.Ut;
import com.babTing.toto.demo.vo.Rq;

@Component
public class NeedLoginIntercepter implements HandlerInterceptor {

	@Autowired
	private Rq rq;
	
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
		resp.setContentType("text/html; charset=UTF-8");
		Rq rq = (Rq) req.getAttribute("rq");
		if (!rq.isLogined()) {
			rq.printHistoryBackJs(Ut.jsHistoryBack("F-A", "로그인을 해주세요."));
			return false;
		}
		
		return HandlerInterceptor.super.preHandle(req, resp, handler);
	}
}