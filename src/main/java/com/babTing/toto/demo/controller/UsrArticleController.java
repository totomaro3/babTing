package com.babTing.toto.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.babTing.toto.demo.service.ArticleService;
import com.babTing.toto.demo.service.BoardService;
import com.babTing.toto.demo.service.ReactionPointService;
import com.babTing.toto.demo.service.ReplyService;
import com.babTing.toto.demo.util.Ut;
import com.babTing.toto.demo.vo.Article;
import com.babTing.toto.demo.vo.Board;
import com.babTing.toto.demo.vo.Reply;
import com.babTing.toto.demo.vo.ResultData;
import com.babTing.toto.demo.vo.Rq;

@Controller
public class UsrArticleController {

	@Autowired
	private ArticleService articleService;
	@Autowired
	private BoardService boardService;
	@Autowired
	private ReplyService replyService;
	@Autowired
	private ReactionPointService reactionPointService;
	@Autowired
	private Rq rq;

	/**
	 * 게시글 불러오기 (공지, 밥팅, 자유, 문의)
	 * 
	 * @param model
	 * @param boardId
	 * @param page
	 * @param searchKeywordTypeCode
	 * @param searchKeyword
	 * @return list 이동
	 */
	@RequestMapping("/usr/article/list")
	public String showList(Model model, int boardId, int page,
			@RequestParam(defaultValue = "") String searchKeywordTypeCode,
			@RequestParam(defaultValue = "") String searchKeyword) {
		Board board = boardService.getBoardById(boardId);

		int articlesCount = articleService.getArticlesCount(boardId, searchKeywordTypeCode, searchKeyword);

		int itemsInAPage = 10;
		int limitFrom = (page - 1) * itemsInAPage;
		int pagesCount = (int) Math.ceil((double) articlesCount / itemsInAPage);

		ResultData<List<Article>> getArticlesRd = articleService.getArticles(boardId, limitFrom, itemsInAPage,
				searchKeywordTypeCode, searchKeyword);

		List<Article> articles = getArticlesRd.getData1();

		if (board == null && boardId != 0) {
			return rq.jsHitoryBackOnView("존재하지 않는 게시판 입니다.");
		}

		model.addAttribute("board", board);
		model.addAttribute("boardId", boardId);
		model.addAttribute("articles", articles);
		model.addAttribute("articlesCount", articlesCount);
		model.addAttribute("page", page);
		model.addAttribute("pagesCount", pagesCount);
		model.addAttribute("searchKeywordTypeCode", searchKeywordTypeCode);
		model.addAttribute("searchKeyword", searchKeyword);

		// ResultData.from("S-1", "게시글 목록을 조회합니다.","articles", articles);
		return "usr/article/list";
	}

	/**
	 * 나의 밥팅 보기
	 * 
	 * @param model
	 * @param boardId
	 * @param page
	 * @param searchKeywordTypeCode
	 * @param searchKeyword
	 * @return list 이동
	 */
	@RequestMapping("/usr/article/myList")
	public String showMyList(Model model, int boardId, int page,
			@RequestParam(defaultValue = "") String searchKeywordTypeCode,
			@RequestParam(defaultValue = "") String searchKeyword) {

		Board board = boardService.getBoardById(boardId);

		int articlesCount = articleService.getArticlesCount(boardId, searchKeywordTypeCode, searchKeyword);

		int itemsInAPage = 10;
		int limitFrom = (page - 1) * itemsInAPage;
		int pagesCount = (int) Math.ceil((double) articlesCount / itemsInAPage);

		int loginedMemberId = rq.getLoginedMemberId();

		ResultData<List<Article>> getArticlesRd = articleService.getMyArticles(limitFrom, itemsInAPage,
				searchKeywordTypeCode, searchKeyword, loginedMemberId);

		List<Article> articles = getArticlesRd.getData1();

		if (board == null && boardId != 0) {
			return rq.jsHitoryBackOnView("존재하지 않는 게시판 입니다.");
		}

		model.addAttribute("board", board);
		model.addAttribute("boardId", boardId);
		model.addAttribute("articles", articles);
		model.addAttribute("articlesCount", articlesCount);
		model.addAttribute("page", page);
		model.addAttribute("pagesCount", pagesCount);
		model.addAttribute("searchKeywordTypeCode", searchKeywordTypeCode);
		model.addAttribute("searchKeyword", searchKeyword);

		// ResultData.from("S-1", "게시글 목록을 조회합니다.","articles", articles);
		return "usr/article/list";
	}

	/**
	 * 게시글 상세보기
	 * 
	 * @param model
	 * @param id
	 * @return datail 이동
	 */
	@RequestMapping("/usr/article/detail")
	public String showDetail(Model model, int id) {

		ResultData<Article> getArticleRd = articleService.getArticle(id);
		Article article = getArticleRd.getData1();

		List<Reply> replies = replyService.getReplies(rq.getLoginedMemberId(), "article", id);
		int repliesCount = replies.size();

		if (article == null) {
			// ResultData.from("F-1", id + "번글은 존재하지 않습니다.");
			return rq.jsHitoryBackOnView(id + "번글은 존재하지 않습니다.");
		}
		// ResultData.from("S-1", id+"번글을 조회합니다.","String", sb.toString());

		boolean actorCanMakeReaction = reactionPointService.actorCanMakeReaction(rq.getLoginedMemberId(), "article",
				id);

		boolean actorHasGoodReaction = reactionPointService.actorHasGoodReaction(rq.getLoginedMemberId(), "article",
				id);

		boolean actorHasBadReaction = reactionPointService.actorHasBadReaction(rq.getLoginedMemberId(), "article", id);

		model.addAttribute("repliesCount", repliesCount);
		model.addAttribute("article", article);
		model.addAttribute("replies", replies);
		model.addAttribute("actorCanMakeReaction", actorCanMakeReaction);
		model.addAttribute("actorHasGoodReaction", actorHasGoodReaction);
		model.addAttribute("actorHasBadReaction", actorHasBadReaction);

		return "usr/article/detail";
	}

	/**
	 * 게시글 작성 폼 불러오기 (공지, 밥팅 , 자유 , 문의)
	 * @param model
	 * @return write 이동
	 */
	@RequestMapping("/usr/article/write")
	public String showWrite(Model model) {

		int loginedMemberId = rq.getLoginedMemberId();

		model.addAttribute("loginedMemberId", loginedMemberId);

		return "usr/article/write";
	}

	/**
	 * 게시글 추가 (INSERT)
	 * @param title
	 * @param body
	 * @param boardId
	 * @param restaurantName
	 * @param address
	 * @param deliveryCost
	 * @param latitude
	 * @param longitude
	 * @return 작성 알림
	 */
	@RequestMapping("/usr/article/doWrite")
	@ResponseBody
	public String doWrite(String title, String body, int boardId,
			@RequestParam(defaultValue = "") String restaurantName, @RequestParam(defaultValue = "") String address,
			@RequestParam(defaultValue = "0") int deliveryCost, @RequestParam(defaultValue = "0") double latitude,
			@RequestParam(defaultValue = "0") double longitude) {

		if (Ut.empty(title)) {
			return Ut.jsHistoryBack("F-1", "제목을 입력해주세요");
		}
		if (Ut.empty(body)) {
			return Ut.jsHistoryBack("F-2", "내용을 입력해주세요");
		}

		int loginedMemberId = rq.getLoginedMemberId();

		ResultData<Integer> writeArticleRd = articleService.writeArticle(title, body, loginedMemberId, boardId,
				restaurantName, address, deliveryCost, latitude, longitude);

		int id = (int) writeArticleRd.getData1();

		ResultData<Article> getArticleRd = articleService.getArticle(id);

		Article article = getArticleRd.getData1();

		// ResultData.newData(writeArticleRd, "String", sb.toString());

		return Ut.jsReplace("S-1", id + "번글이 작성되었습니다.", "detail?id=" + id);
	}

	/**
	 * 게시글 수정 폼 불러오기
	 * @param model
	 * @param id
	 * @return modify 이동
	 */
	@RequestMapping("/usr/article/modify")
	public String showModify(Model model, int id) {

		ResultData<Article> getArticleRd = articleService.getArticle(id);

		Article article = getArticleRd.getData1();

		if (article == null) {
			// ResultData.from("F-1", id + "번글은 존재하지 않습니다.");
			return rq.jsHitoryBackOnView(id + "번글은 존재하지 않습니다.");
		}

		int loginedMemberId = rq.getLoginedMemberId();

		if (article.getMemberId() != loginedMemberId) {
			// ResultData.from("F-2", Ut.f("해당 글에 대한 권한이 없습니다."));
			return rq.jsHitoryBackOnView("해당 글에 대한 권한이 없습니다.");
		}

		model.addAttribute("article", article);

		return "usr/article/modify";
	}

	/**
	 * 게시글 수정 (UPDATE)
	 * @param id
	 * @param title
	 * @param body
	 * @param boardId
	 * @param restaurantName
	 * @param deliveryCost
	 * @param latitude
	 * @param longitude
	 * @return 수정 알림
	 */
	@RequestMapping("/usr/article/doModify")
	@ResponseBody
	public String doModify(int id, String title, String body, int boardId, String restaurantName, int deliveryCost,
			double latitude, double longitude) {

		ResultData<Article> getArticleRd = articleService.getArticle(id);

		Article article = getArticleRd.getData1();

		if (article == null) {
			return Ut.jsHistoryBack("F-1", id + "번글은 존재하지 않습니다.");
		}

		int loginedMemberId = rq.getLoginedMemberId();

		if (article.getMemberId() != loginedMemberId) {
			return Ut.jsHistoryBack("F-2", "해당 글에 대한 권한이 없습니다.");
		}

		articleService.doModifyArticle(id, title, body, boardId, restaurantName, deliveryCost, latitude, longitude);

		// ResultData.from("S-1", id + "번글이 수정되었습니다.", "article", article);
		return Ut.jsReplace("S-1", id + "번글이 수정되었습니다.", "detail?id=" + id);
	}

	/**
	 * 게시글 삭제 (DELETE)
	 * @param id
	 * @return 삭제 알림
	 */
	@RequestMapping("/usr/article/doDelete")
	@ResponseBody
	public String doDelete(int id) {

		ResultData<Article> getArticleRd = articleService.getArticle(id);

		Article article = getArticleRd.getData1();

		if (article == null) {
			return Ut.jsHistoryBack("F-1", id + "번글은 존재하지 않습니다.");
		}

		int loginedMemberId = rq.getLoginedMemberId();
		if (article.getMemberId() != loginedMemberId) {
			return Ut.jsHistoryBack("F-2", "해당 글에 대한 권한이 없습니다.");
		}

		int articleBoardId = article.getBoardId();

		articleService.doDeleteArticle(article);

		return Ut.jsReplace("S-1", id + "번글이 삭제되었습니다.", "list?boardId=" + articleBoardId + "&page=1");
	}

	/**
	 * 게시글 마감 (UPDATE) 마감된 게시글은 작성자만 열람
	 * @param id
	 * @return 마감 알림
	 */
	@RequestMapping("/usr/article/doDeadLine")
	@ResponseBody
	public String doDeadLine(int id) {

		ResultData<Article> getArticleRd = articleService.getArticle(id);

		Article article = getArticleRd.getData1();

		if (article == null) {
			return Ut.jsHistoryBack("F-1", id + "번글은 존재하지 않습니다.");
		}

		int loginedMemberId = rq.getLoginedMemberId();
		if (article.getMemberId() != loginedMemberId) {
			return Ut.jsHistoryBack("F-2", "해당 글에 대한 권한이 없습니다.");
		}

		articleService.doDeadArticle(article);

		return Ut.jsReplace("S-1", id + "번글이 마감되었습니다.", "detail?id=" + id);
	}

	/**
	 * 게시글 마감 취소 (UPDATE)
	 * @param id
	 * @return 마감 취소 알림
	 */
	@RequestMapping("/usr/article/doCancelDeadArticle")
	@ResponseBody
	public String doCancleDeadLine(int id) {

		ResultData<Article> getArticleRd = articleService.getArticle(id);

		Article article = getArticleRd.getData1();

		if (article == null) {
			return Ut.jsHistoryBack("F-1", id + "번글은 존재하지 않습니다.");
		}

		int loginedMemberId = rq.getLoginedMemberId();
		if (article.getMemberId() != loginedMemberId) {
			return Ut.jsHistoryBack("F-2", "해당 글에 대한 권한이 없습니다.");
		}

		articleService.doCancelDeadArticle(article);

		return Ut.jsReplace("S-1", id + "번글이 마감 취소되었습니다.", "detail?id=" + id);
	}

	/**
	 * 조회 수 증가 Ajax
	 * @param id
	 * @return 
	 */
	@RequestMapping("/usr/article/doIncreaseHitCountRd")
	@ResponseBody
	public ResultData<Integer> doIncreaseHitCountRd(int id) {

		ResultData<Integer> increaseHitCountRd = articleService.increaseHitCount(id);

		if (increaseHitCountRd.isFail()) {
			return increaseHitCountRd;
		}

		return ResultData.newData(increaseHitCountRd, "hitCount", articleService.getArticleHitCount(id));
	}
}