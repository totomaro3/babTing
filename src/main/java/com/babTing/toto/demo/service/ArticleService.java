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

	public ResultData<Article> getArticle(int id) {

		Article article = articleRepository.getArticle(id);
		
		return ResultData.from("S-1", Ut.f("%d번 글을 불러왔습니다.", id),"article", article);
	}
	
	public ResultData<List<Article>> getArticles(int boardId, int limitFrom, int itemsInAPage, String searchKeywordTypeCode, String searchKeyword) {
		
		List<Article> articles = articleRepository.getArticles(boardId, limitFrom, itemsInAPage, searchKeywordTypeCode, searchKeyword);
		
		return ResultData.from("S-1", Ut.f("모든 글을 불러왔습니다."),"articles", articles);
	}
	
	public ResultData<Integer> writeArticle(String title, String body, int memberId, int boardId,
			String restaurantName, int distance, int deliveryCost, double latitude, double longitude) {

		articleRepository.writeArticle(title, body, memberId, boardId, restaurantName, distance , deliveryCost, latitude, longitude);

		int id = articleRepository.getLastInsertId();

		return ResultData.from("S-1", Ut.f("%d번 글이 생성되었습니다", id),"id", id);
		
	}
	
	public void doDeleteArticle(Article article) {
		articleRepository.doDeleteArticle(article);
	}

	public ResultData<Integer> doModifyArticle(int id, String title, String body) {
		
		articleRepository.doModifyArticle(id,title,body);
		
		return ResultData.from("S-1", Ut.f("%d번 글이 수정되었습니다", id),"id", id);
	}

	public int getArticlesCount(int boardId, String searchKeywordTypeCode, String searchKeyword) {
		return articleRepository.getArticlesCount(boardId, searchKeywordTypeCode, searchKeyword);
	}

	public ResultData<Integer>  increaseHitCount(int id) {
		int affectedRow = articleRepository.increaseHitCount(id);

		if (affectedRow == 0) {
			return ResultData.from("F-1", "해당 게시물은 없음", "affectedRow", affectedRow);
		}

		return ResultData.from("S-1", "조회수 증가", "affectedRow", affectedRow);
	}

	public int getArticleHitCount(int id) {
		return articleRepository.getArticleHitCount(id);
	}

	public ResultData<Integer> increaseGoodReactionPoint(int relId) {
		int affectedRow = articleRepository.increaseGoodReactionPoint(relId);

		if (affectedRow == 0) {
			return ResultData.from("F-1", "해당 게시물은 없습니다", "affectedRow", affectedRow);
		}
		return ResultData.from("S-1", "좋아요 증가", "affectedRow", affectedRow);
	}

	public ResultData<Integer>  increaseBadReactionPoint(int relId) {

		int affectedRow = articleRepository.increaseBadReactionPoint(relId);

		if (affectedRow == 0) {
			return ResultData.from("F-1", "해당 게시물은 없습니다", "affectedRow", affectedRow);
		}
		return ResultData.from("S-1", "싫어요 증가", "affectedRow", affectedRow);
	}

	public ResultData<Integer>  decreaseGoodReationPoint(int relId) {
		int affectedRow = articleRepository.decreaseGoodReactionPoint(relId);

		if (affectedRow == 0) {
			return ResultData.from("F-1", "해당 게시물은 없습니다", "affectedRow", affectedRow);
		}
		return ResultData.from("S-1", "좋아요 취소", "affectedRow", affectedRow);
	}
	
	public ResultData<Integer>  decreaseBadReationPoint(int relId) {
		int affectedRow = articleRepository.decreaseBadReactionPoint(relId);

		if (affectedRow == 0) {
			return ResultData.from("F-1", "해당 게시물은 없습니다", "affectedRow", affectedRow);
		}
		return ResultData.from("S-1", "좋아요 취소", "affectedRow", affectedRow);
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
