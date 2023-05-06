package com.babTing.toto.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.babTing.toto.demo.service.ArticleService;
import com.babTing.toto.demo.vo.Article;
import com.babTing.toto.demo.vo.Rq;

@Controller
public class UsrHomeController {
	@Autowired
	private Rq rq;
	@Autowired
	private ArticleService articleService;
	
	@RequestMapping("/usr/home/main")
	public String showMain(Model model) {
		
		int itemsInAPage = 5;
		int limitFrom = 0;
		
		List<Article> noticeArticles;
		List<Article> babtingArticles;
		List<Article> freeArticles;
		
		noticeArticles = articleService.getNoticeArticles(limitFrom, itemsInAPage);
		babtingArticles = articleService.getBabtingArticles(limitFrom, itemsInAPage);
		freeArticles = articleService.getFreeArticles(limitFrom, itemsInAPage);

		model.addAttribute("noticeArticles", noticeArticles);
		model.addAttribute("babtingArticles", babtingArticles);
		model.addAttribute("freeArticles", freeArticles);

		// ResultData.from("S-1", "게시글 목록을 조회합니다.","articles", articles);
		return "usr/home/main";
	}

	@RequestMapping("/")
	public String showRoot() {
		return "redirect:/usr/home/main";
	}

}