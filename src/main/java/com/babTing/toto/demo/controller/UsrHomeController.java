package com.babTing.toto.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.babTing.toto.demo.service.ArticleService;
import com.babTing.toto.demo.vo.Article;
import com.babTing.toto.demo.vo.Member;
import com.babTing.toto.demo.vo.Rq;

@Controller
public class UsrHomeController {
	@Autowired
	private Rq rq;
	@Autowired
	private ArticleService articleService;
	
	/**
	 * 메인페이지 (밥팅, 공지사항, 자유게시판을 메인페이지에 보여줌)
	 * @param model
	 * @return 메인 이동
	 */
	@RequestMapping("/usr/home/main")
	public String showMain(Model model) {
		
		int itemsInAPage = 5;
		int limitFrom = 0;
		
		Member loginedMember = rq.getLoginedMember();
		String[] customKeyword = {null, null, null, null, null};
		
		if(loginedMember != null) {
			customKeyword[0] = loginedMember.getKeyword1();
			customKeyword[1] = loginedMember.getKeyword2();
			customKeyword[2] = loginedMember.getKeyword3();
			customKeyword[3] = loginedMember.getKeyword4();
			customKeyword[4] = loginedMember.getKeyword5();
		}
		
		
		List<Article> noticeArticles;
		List<Article> myBabtingArticles;
		List<Article> babtingArticles;
		List<Article> freeArticles;
		
		noticeArticles = articleService.getNoticeArticles(limitFrom, itemsInAPage);
		myBabtingArticles = articleService.getMyBabtingArticles("title", customKeyword, limitFrom, itemsInAPage);
		babtingArticles = articleService.getBabtingArticles(limitFrom, itemsInAPage);
		freeArticles = articleService.getFreeArticles(limitFrom, itemsInAPage);

		model.addAttribute("noticeArticles", noticeArticles);
		model.addAttribute("myBabtingArticles", myBabtingArticles);
		model.addAttribute("babtingArticles", babtingArticles);
		model.addAttribute("freeArticles", freeArticles);


		return "usr/home/main";
	}

	/**
	 * 메인페이지 편리한 이동
	 * @return
	 */
	@RequestMapping("/")
	public String showRoot() {
		return "redirect:/usr/home/main";
	}
	
	/**
	 * 지도 보여주기 (글 작성 , 회원 가입 시 사용)
	 * @param model
	 * @return 카카오맵 이동
	 */
	@RequestMapping("/usr/home/kakaoMap")
	public String showKakaoMap(Model model) {

		return "usr/home/kakaoMap";
	}
}