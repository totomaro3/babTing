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

	@RequestMapping("/usr/article/write")
	public String showWrite(Model model) {

		int loginedMemberId = rq.getLoginedMemberId();

		model.addAttribute("loginedMemberId", loginedMemberId);

		// ResultData.newData(writeArticleRd, "String", sb.toString());

		return "usr/article/write";
	}

	@RequestMapping("/usr/article/doWrite")
	@ResponseBody
	public String doWrite(String title, String body, int boardId,
			@RequestParam(defaultValue = "") String restaurantName, @RequestParam(defaultValue = "0") int deliveryCost,
			@RequestParam(defaultValue = "0") double latitude, @RequestParam(defaultValue = "0") double longitude) {

		if (Ut.empty(title)) {
			// ResultData.from("F-1", "제목을 입력해주세요");
			return Ut.jsHistoryBack("F-1", "제목을 입력해주세요");
		}
		if (Ut.empty(body)) {
			// ResultData.from("F-2", "내용을 입력해주세요");
			return Ut.jsHistoryBack("F-2", "내용을 입력해주세요");
		}

		int loginedMemberId = rq.getLoginedMemberId();

		ResultData<Integer> writeArticleRd = articleService.writeArticle(title, body, loginedMemberId, boardId,
				restaurantName, deliveryCost, latitude, longitude);

		int id = (int) writeArticleRd.getData1();

		ResultData<Article> getArticleRd = articleService.getArticle(id);

		Article article = getArticleRd.getData1();

		// ResultData.newData(writeArticleRd, "String", sb.toString());

		return Ut.jsReplace("S-1", id + "번글이 작성되었습니다.", "detail?id=" + id);
	}

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

		articleService.doModifyArticle(id, title, body, boardId, restaurantName, deliveryCost,
				latitude, longitude);

		// ResultData.from("S-1", id + "번글이 수정되었습니다.", "article", article);
		return Ut.jsReplace("S-1", id + "번글이 수정되었습니다.", "detail?id=" + id);
	}

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

		articleService.doDeleteArticle(article);

		return Ut.jsReplace("S-1", id + "번글이 삭제되었습니다.", "list?boardId=0&page=1");
	}

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