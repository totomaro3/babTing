package com.babTing.toto.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.babTing.toto.demo.vo.Article;

@Mapper
public interface ArticleRepository {

	public Article getArticle(int id);

	public List<Article> getArticles(int boardId, int limitFrom, int itemsInAPage, String searchKeywordTypeCode, String searchKeyword);
	
	public int getArticlesCount(int boardId, String searchKeywordTypeCode, String searchKeyword );

	public int getLastInsertId();

	public void writeArticle(String title, String body, int memberId, int boardId);

	public void doDeleteArticle(Article article);

	public void doModifyArticle(int id, String title, String body);

	public String getwriterName(int id);

	public void increaseHitCount(int id);

	@Select("""
			SELECT A.*, M.nickname AS extra__writer
			FROM article AS A
			INNER JOIN `member` AS M
			ON A.memberId = M.id
			WHERE A.boardId = 1
			ORDER BY A.id DESC
			LIMIT #{limitFrom}, #{itemsInAPage};
			""")
	public List<Article> getNoticeArticles(int limitFrom, int itemsInAPage);

	@Select("""
			SELECT A.*, M.nickname AS extra__writer
			FROM article AS A
			INNER JOIN `member` AS M
			ON A.memberId = M.id
			WHERE A.boardId = 2
			ORDER BY A.id DESC
			LIMIT #{limitFrom}, #{itemsInAPage};
			""")
	public List<Article> getBabtingArticles(int limitFrom, int itemsInAPage);

	@Select("""
			SELECT A.*, M.nickname AS extra__writer
			FROM article AS A
			INNER JOIN `member` AS M
			ON A.memberId = M.id
			WHERE A.boardId = 3
			ORDER BY A.id DESC
			LIMIT #{limitFrom}, #{itemsInAPage};
			""")
	public List<Article> getFreeArticles(int limitFrom, int itemsInAPage);
}