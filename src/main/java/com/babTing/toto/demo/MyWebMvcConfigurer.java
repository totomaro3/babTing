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
	// BeforeActionInterceptor 불러오기
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
		.excludePathPatterns("/resource/**").excludePathPatterns("/error");
		
		registry.addInterceptor(needLoginInterceptor)
		.addPathPatterns("/usr/article/write")
		.addPathPatterns("/usr/article/doWrite")
		.addPathPatterns("/usr/article/modify")
		.addPathPatterns("/usr/article/doModify")
		.addPathPatterns("/usr/article/doDelete")
		.addPathPatterns("/usr/member/doLogout")
		.excludePathPatterns("/resource/**").excludePathPatterns("/error");
		
		registry.addInterceptor(needLogoutIntercepter)
		.addPathPatterns("/usr/member/login")
		.addPathPatterns("/usr/member/doLogin")
		.addPathPatterns("/usr/member/doJoin")
		.excludePathPatterns("/resource/**").excludePathPatterns("/error");
	}

}