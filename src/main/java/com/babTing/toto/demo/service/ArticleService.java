package com.babTing.toto.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.babTing.toto.demo.repository.ArticleRepository;
import com.babTing.toto.demo.util.Ut;
import com.babTing.toto.demo.vo.Article;
import com.babTing.toto.demo.vo.ResultData;
import com.babTing.toto.demo.vo.Room;

@Service
public class ArticleService {

	@Autowired
	ArticleRepository articleRepository;

	/**
	 * 글 하나를 불러옵니다.
	 * 
	 * @param id
	 * @return article
	 */
	public ResultData<Article> getArticle(int id) {

		Article article = articleRepository.getArticle(id);

		return ResultData.from("S-1", Ut.f("%d번 글을 불러왔습니다.", id), "article", article);
	}

	/**
	 * 글 전체를 불러옵니다.
	 * 
	 * @param boardId
	 * @param limitFrom
	 * @param itemsInAPage
	 * @param searchKeywordTypeCode
	 * @param searchKeyword
	 * @return articles
	 */
	public ResultData<List<Article>> getArticles(int boardId, int limitFrom, int itemsInAPage,
			String searchKeywordTypeCode, String searchKeyword) {

		List<Article> articles = articleRepository.getArticles(boardId, limitFrom, itemsInAPage, searchKeywordTypeCode,
				searchKeyword);

		return ResultData.from("S-1", Ut.f("모든 글을 불러왔습니다."), "articles", articles);
	}

	/**
	 * 내가 작성한 글 전체를 불러옵니다.
	 * 
	 * @param limitFrom
	 * @param itemsInAPage
	 * @param searchKeywordTypeCode
	 * @param searchKeyword
	 * @param loginedMemberId
	 * @return articles
	 */
	public ResultData<List<Article>> getMyArticles(int limitFrom, int itemsInAPage, String searchKeywordTypeCode,
			String searchKeyword, int loginedMemberId) {
		
		List<Article> articles = articleRepository.getMyArticles(limitFrom, itemsInAPage, searchKeywordTypeCode,
				searchKeyword, loginedMemberId);

		return ResultData.from("S-1", Ut.f("내 글을 모두 불러왔습니다."), "articles", articles);
	}
	
	public ResultData<List<Article>> getCustomArticles(int boardId, int limitFrom, int itemsInAPage,
			String searchKeywordTypeCode, String[] customKeyword) {
		
		List<Article> articles = articleRepository.getCustomArticles(boardId, limitFrom, itemsInAPage, searchKeywordTypeCode,
				customKeyword);
		
		return ResultData.from("S-1", Ut.f("내 글을 모두 불러왔습니다."), "articles", articles);
	}


	/**
	 * 글을 작성합니다.
	 * 
	 * @param title
	 * @param body
	 * @param memberId
	 * @param boardId
	 * @param restaurantName
	 * @param address
	 * @param deliveryCost
	 * @param latitude
	 * @param longitude
	 * @return id
	 */
	public ResultData<Integer> writeArticle(String title, String body, int memberId, int boardId, String restaurantName,
			String address, String category, int deliveryCost, double latitude, double longitude) {

		articleRepository.writeArticle(title, body, memberId, boardId, restaurantName, address, category, deliveryCost, latitude,
				longitude);

		int id = articleRepository.getLastInsertId();

		return ResultData.from("S-1", Ut.f("%d번 글이 생성되었습니다", id), "id", id);

	}

	/**
	 * 글을 삭제합니다.
	 * 
	 * @param article
	 */
	public void doDeleteArticle(Article article) {
		articleRepository.doDeleteArticle(article);
	}

	/**
	 * 글을 마감합니다.
	 * 
	 * @param article
	 */
	public void doDeadArticle(Article article) {
		articleRepository.doDeadArticle(article);
	}

	/**
	 * 글을 마감 취소합니다.
	 * 
	 * @param article
	 */
	public void doCancelDeadArticle(Article article) {
		articleRepository.doCancelDeadArticle(article);
	}

	/**
	 * 글을 수정합니다.
	 * 
	 * @param id
	 * @param title
	 * @param body
	 * @param boardId
	 * @param restaurantName
	 * @param deliveryCost
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public ResultData<Integer> doModifyArticle(int id, String title, String body, int boardId, String restaurantName,
			String address, String category, int deliveryCost, double latitude, double longitude) {

		articleRepository.doModifyArticle(id, title, body, boardId, restaurantName, address, category, deliveryCost, latitude,
				longitude);

		return ResultData.from("S-1", Ut.f("%d번 글이 수정되었습니다", id), "id", id);
	}

	/**
	 * 글의 갯수를 구합니다. (pagenation)
	 * 
	 * @param boardId
	 * @param searchKeywordTypeCode
	 * @param searchKeyword
	 * @return int
	 */
	public int getArticlesCount(int boardId, String searchKeywordTypeCode, String searchKeyword) {
		return articleRepository.getArticlesCount(boardId, searchKeywordTypeCode, searchKeyword);
	}

	/**
	 * 조회수 증가
	 * 
	 * @param id
	 * @return affectedRow
	 */
	public ResultData<Integer> increaseHitCount(int id) {
		int affectedRow = articleRepository.increaseHitCount(id);

		if (affectedRow == 0) {
			return ResultData.from("F-1", "해당 게시물은 없음", "affectedRow", affectedRow);
		}

		return ResultData.from("S-1", "조회수 증가", "affectedRow", affectedRow);
	}

	/**
	 * 조회수 가져오기
	 * 
	 * @param id
	 * @return
	 */
	public int getArticleHitCount(int id) {
		return articleRepository.getArticleHitCount(id);
	}

	/**
	 * 추천 관련
	 */

	/**
	 * 좋아요 증가
	 * 
	 * @param relId
	 * @return
	 */
	public ResultData<Integer> increaseGoodReactionPoint(int relId) {
		int affectedRow = articleRepository.increaseGoodReactionPoint(relId);

		if (affectedRow == 0) {
			return ResultData.from("F-1", "해당 게시물은 없습니다", "affectedRow", affectedRow);
		}
		return ResultData.from("S-1", "좋아요 증가", "affectedRow", affectedRow);
	}

	/**
	 * 싫어요 증가
	 * 
	 * @param relId
	 * @return
	 */
	public ResultData<Integer> increaseBadReactionPoint(int relId) {

		int affectedRow = articleRepository.increaseBadReactionPoint(relId);

		if (affectedRow == 0) {
			return ResultData.from("F-1", "해당 게시물은 없습니다", "affectedRow", affectedRow);
		}
		return ResultData.from("S-1", "싫어요 증가", "affectedRow", affectedRow);
	}

	/**
	 * 좋아요 감소
	 * 
	 * @param relId
	 * @return
	 */
	public ResultData<Integer> decreaseGoodReationPoint(int relId) {
		int affectedRow = articleRepository.decreaseGoodReactionPoint(relId);

		if (affectedRow == 0) {
			return ResultData.from("F-1", "해당 게시물은 없습니다", "affectedRow", affectedRow);
		}
		return ResultData.from("S-1", "좋아요 취소", "affectedRow", affectedRow);
	}

	/**
	 * 싫어요 감소
	 * 
	 * @param relId
	 * @return
	 */
	public ResultData<Integer> decreaseBadReationPoint(int relId) {
		int affectedRow = articleRepository.decreaseBadReactionPoint(relId);

		if (affectedRow == 0) {
			return ResultData.from("F-1", "해당 게시물은 없습니다", "affectedRow", affectedRow);
		}
		return ResultData.from("S-1", "좋아요 취소", "affectedRow", affectedRow);
	}

	/**
	 * 메인 페이지에서 사용
	 */

	/**
	 * 
	 * 공지사항만 가져오기
	 * 
	 * @param limitFrom
	 * @param itemsInAPage
	 * @return
	 */
	public List<Article> getNoticeArticles(int limitFrom, int itemsInAPage) {

		return articleRepository.getNoticeArticles(limitFrom, itemsInAPage);
	}

	/**
	 * 밥팅만 가져오기
	 * 
	 * @param limitFrom
	 * @param itemsInAPage
	 * @return
	 */
	public List<Article> getBabtingArticles(int limitFrom, int itemsInAPage) {

		return articleRepository.getBabtingArticles(limitFrom, itemsInAPage);
	}

	/**
	 * 맞춤 밥팅만 가져오기
	 * 
	 * @param string
	 * @param string2
	 * @param limitFrom
	 * @param itemsInAPage
	 * @return
	 */
	public List<Article> getMyBabtingArticles(String searchKeywordTypeCode, String[] customKeyword, int limitFrom,
			int itemsInAPage) {

		return articleRepository.getMyBabtingArticles(searchKeywordTypeCode, customKeyword, limitFrom, itemsInAPage);
	}

	/**
	 * 자유글만 가져오기
	 * 
	 * @param limitFrom
	 * @param itemsInAPage
	 * @return
	 */
	public List<Article> getFreeArticles(int limitFrom, int itemsInAPage) {
		return articleRepository.getFreeArticles(limitFrom, itemsInAPage);
	}
}
