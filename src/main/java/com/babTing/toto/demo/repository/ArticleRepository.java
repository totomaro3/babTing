package com.babTing.toto.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.babTing.toto.demo.vo.Article;

@Mapper
public interface ArticleRepository {

	@Select("""
			<script>
			SELECT A.*,
			M.nickname AS extra__writer,
			M.longitude AS extra__writerLongitude,
			M.latitude AS extra__writerLatitude,
			IFNULL(SUM(RP.point),0) AS extra__sumReactionPoint,
			IFNULL(SUM(IF(RP.point &gt; 0,RP.point,0)),0) AS extra__goodReactionPoint,
			IFNULL(SUM(IF(RP.point &lt; 0,RP.point,0)),0) AS extra__badReactionPoint
			FROM article AS A
			INNER JOIN `member` AS M
			ON A.memberId = M.id
			LEFT JOIN reactionPoint AS RP
			ON A.id = RP.relId AND RP.relTypeCode = 'article'
			WHERE A.id = #{id}
			GROUP BY A.id
			</script>
			""")
	public Article getArticle(int id);

	@Select("""
			<script>
			SELECT A.*,
			M.nickname AS extra__writer,
			M.longitude AS extra__writerLongitude,
			M.latitude AS extra__writerLatitude,
			IFNULL(SUM(RP.point),0) AS extra__sumReactionPoint,
			IFNULL(SUM(IF(RP.point &gt; 0,RP.point,0)),0) AS extra__goodReactionPoint,
			IFNULL(SUM(IF(RP.point &lt; 0,RP.point,0)),0) AS extra__badReactionPoint
			FROM article AS A
			INNER JOIN `member` AS M
			ON A.memberId = M.id
			LEFT JOIN reactionPoint AS RP
			ON A.id = RP.relId AND RP.relTypeCode = 'article'
			WHERE 1
			AND A.BoardId = #{boardId}
			<if test="boardId == 2">
			AND A.deadlineTime &gt; NOW()
			</if>
			<if test="searchKeyword != ''">
				<choose>
					<when test="searchKeywordTypeCode == 'title'" >
						AND A.title LIKE CONCAT('%',#{searchKeyword},'%')
					</when>
					<when test="searchKeywordTypeCode == 'body'" >
						AND A.body LIKE CONCAT('%',#{searchKeyword},'%')
					</when>
					<otherwise>
						AND A.title LIKE CONCAT('%',#{searchKeyword},'%')
						OR A.body LIKE CONCAT('%',#{searchKeyword},'%')
					</otherwise>
				</choose>
			</if>
			GROUP BY A.id
			ORDER BY A.id DESC
			LIMIT #{limitFrom}, #{itemsInAPage};
			</script>
			""")
	public List<Article> getArticles(int boardId, int limitFrom, int itemsInAPage, String searchKeywordTypeCode,
			String searchKeyword);

	@Select("""
			<script>
			SELECT Count(*) AS cnt
			FROM article AS A
			WHERE 1
			<if test="boardId != 0">
				AND A.BoardId = #{boardId}
			</if>
				<if test="searchKeyword != ''">
				<choose>
					<when test="searchKeywordTypeCode == 'title'" >
						AND A.title LIKE CONCAT('%',#{searchKeyword},'%')
					</when>
					<when test="searchKeywordTypeCode == 'body'" >
						AND A.body LIKE CONCAT('%',#{searchKeyword},'%')
					</when>
					<otherwise>
						AND A.title LIKE CONCAT('%',#{searchKeyword},'%')
						OR A.body LIKE CONCAT('%',#{searchKeyword},'%')
					</otherwise>
				</choose>
			</if>
			</script>
			""")
	public int getArticlesCount(int boardId, String searchKeywordTypeCode, String searchKeyword);

	@Select("""
			<script>
			SELECT LAST_INSERT_ID()
			</script>
			""")
	public int getLastInsertId();

	@Insert("""
				<script>
			INSERT INTO article
			SET regDate = NOW(),
			updateDate= NOW(),
			<if test="boardId == 2">
				restaurantName = #{restaurantName},
				latitude = #{latitude},
				longitude = #{longitude},
				deliveryCost = #{deliveryCost},
				deadlineTime = NOW() + INTERVAL 1 DAY,
			</if>
			title =#{title},
			`body`= #{body},
			memberId = #{memberId},
			boardId = #{boardId}

				</script>
				""")
	public void writeArticle(String title, String body, int memberId, int boardId, String restaurantName,
			int deliveryCost, double latitude, double longitude);

	@Delete("""
			<script>
			DELETE FROM
			article
			WHERE id = #{id}
			</script>
			""")
	public void doDeleteArticle(Article article);

	@Update("""
			<script>
			UPDATE article
			<set>
			<if test="title != null and title != ''">title = #{title},</if>
			<if test="body != null and title != ''">`body` = #{body},</if>
			<if test="boardId == 2">
			<if test="restaurantName != null and restaurantName != ''">restaurantName = #{restaurantName},</if>
			</if>
			updateDate= NOW(),
			</set>
			WHERE id = #{id}
			</script>
			""")
	public void doModifyArticle(int id, String title, String body, int boardId, String restaurantName, int deliveryCost,
			double latitude, double longitude);

	@Select("""
			<script>
			SELECT hitCount
			FROM article
			WHERE id = #{id}
			</script>
			""")
	public int getArticleHitCount(int id);

	@Update("""
			<script>
			UPDATE article
			SET hitCount = hitCount + 1
			WHERE id = #{id}
			</script>
			""")
	public int increaseHitCount(int id);

	@Update("""
			<script>
			UPDATE article
			SET goodReactionPoint = goodReactionPoint + 1
			WHERE id = #{relId}
			</script>
			""")
	public int increaseGoodReactionPoint(int relId);

	@Update("""
			<script>
			UPDATE article
			SET badReactionPoint = badReactionPoint + 1
			WHERE id = #{relId}
			</script>
			""")
	public int increaseBadReactionPoint(int relId);

	@Update("""
			<script>
			UPDATE article
			SET goodReactionPoint = goodReactionPoint - 1
			WHERE id = #{relId}
			</script>
			""")
	public int decreaseGoodReactionPoint(int relId);

	@Update("""
			<script>
			UPDATE article
			SET badReactionPoint = badReactionPoint - 1
			WHERE id = #{relId}
			</script>
			""")
	public int decreaseBadReactionPoint(int relId);

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
			<script>
			SELECT A.*, M.nickname AS extra__writer,
			M.longitude AS extra__writerLongitude,
			M.latitude AS extra__writerLatitude
			FROM article AS A
			INNER JOIN `member` AS M
			ON A.memberId = M.id
			WHERE A.boardId = 2
			AND A.deadlineTime &gt; NOW()
			ORDER BY A.id DESC
			LIMIT #{limitFrom}, #{itemsInAPage};
			</script>
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