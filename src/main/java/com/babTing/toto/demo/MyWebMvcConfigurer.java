package com.babTing.toto.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.babTing.toto.demo.interceptor.BeforeActionInterceptor;
import com.babTing.toto.demo.interceptor.NeedLoginIntercepter;
import com.babTing.toto.demo.interceptor.NeedLogoutIntercepter;



@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {

	@Autowired
	BeforeActionInterceptor beforeActionInterceptor;
	
	@Autowired
	NeedLoginIntercepter needLoginInterceptor;
	
	@Autowired
	NeedLogoutIntercepter needLogoutIntercepter;

	// /resource/common.css
	// 인터셉터 적용
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(beforeActionInterceptor)
		.addPathPatterns("/**")
		.addPathPatterns("/favicon.ico")
		.excludePathPatterns("/resource/**").excludePathPatterns("/error");
		
		//로그인이 필요할 때 로그인을 하라고 미리 막아준다
		//url로 억지로 진입을 하려고 할 때 유용
		registry.addInterceptor(needLoginInterceptor)
		.addPathPatterns("/usr/article/detail")
		.addPathPatterns("/usr/article/write")
		.addPathPatterns("/usr/article/doWrite")
		.addPathPatterns("/usr/article/modify")
		.addPathPatterns("/usr/article/doModify")
		.addPathPatterns("/usr/article/doDelete")
		.addPathPatterns("/usr/member/doLogout")
		.addPathPatterns("/usr/member/myPage")
		.addPathPatterns("/usr/member/modify")
		.addPathPatterns("/usr/member/doModify")
		.addPathPatterns("/usr/member/doDelete")
		.addPathPatterns("/usr/reply/**")
		.addPathPatterns("/usr/reactionPoint/**")
		.excludePathPatterns("/resource/**").excludePathPatterns("/error");
		
		//로그아웃이 필요할 때 로그아웃을 하라고 미리 막아준다
		//url로 억지로 진입을 하려고 할 때 유용
		registry.addInterceptor(needLogoutIntercepter)
		.addPathPatterns("/usr/member/login")
		.addPathPatterns("/usr/member/doLogin")
		.addPathPatterns("/usr/member/getLoginIdDup")
		.addPathPatterns("/usr/member/join")
		.addPathPatterns("/usr/member/doJoin")
		.excludePathPatterns("/resource/**").excludePathPatterns("/error");
	}

}