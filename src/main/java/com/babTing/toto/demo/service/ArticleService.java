package com.babTing.toto.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.babTing.toto.demo.repository.ArticleRepository;
import com.babTing.toto.demo.util.Ut;
import com.babTing.toto.demo.vo.Article;
import com.babTing.toto.demo.vo.ResultData;

@Service
public class ArticleService {
	
	@Autowired
	ArticleRepository articleRepository;

	public Article getArticle(int id) {
		
		return articleRepository.getArticle(id);
	}
	
	public List<Article> getArticles(int boardId, int limitFrom, int itemsInAPage, String searchKeywordTypeCode, String searchKeyword) {
		
		return articleRepository.getArticles(boardId, limitFrom, itemsInAPage, searchKeywordTypeCode, searchKeyword);
	}
	
	public ResultData<Integer> writeArticle(String title, String body, int memberId, int boardId) {

		articleRepository.writeArticle(title, body, memberId, boardId);

		int id = articleRepository.getLastInsertId();

		return ResultData.from("S-1", Ut.f("%d번 글이 생성되었습니다", id),"id", id);

	}
	
	public void doDeleteArticle(Article article) {
		articleRepository.doDeleteArticle(article);
	}

	public void doModifyArticle(int id, String title, String body) {
		articleRepository.doModifyArticle(id,title,body);
	}

	public String getwriterName(int id) {
		return articleRepository.getwriterName(id);
	}

	public int getArticlesCount(int boardId, String searchKeywordTypeCode, String searchKeyword) {
		return articleRepository.getArticlesCount(boardId, searchKeywordTypeCode, searchKeyword);
	}

	public void increaseHitCount(int id) {
		articleRepository.increaseHitCount(id);
	}

	public List<Article> getNoticeArticles(int limitFrom, int itemsInAPage) {
		
		return articleRepository.getNoticeArticles(limitFrom, itemsInAPage);
	}

	public List<Article> getBabtingArticles(int limitFrom, int itemsInAPage) {
		
		return articleRepository.getBabtingArticles(limitFrom, itemsInAPage);
	}

	public List<Article> getFreeArticles(int limitFrom, int itemsInAPage) {
		return articleRepository.getFreeArticles(limitFrom, itemsInAPage);
	}
}
