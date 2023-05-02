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
import com.babTing.toto.demo.util.Ut;
import com.babTing.toto.demo.vo.Article;
import com.babTing.toto.demo.vo.Board;
import com.babTing.toto.demo.vo.ResultData;
import com.babTing.toto.demo.vo.Rq;

@Controller
public class UsrArticleController {

	@Autowired
	private ArticleService articleService;
	@Autowired
	private BoardService boardService;
	@Autowired
	private Rq rq;

	@RequestMapping("/usr/article/list")
	public String showList(Model model, int boardId, int page,
			@RequestParam(defaultValue = "") String searchKeywordTypeCode,
			@RequestParam(defaultValue = "") String searchKeyword
			) {
		Board board = boardService.getBoardById(boardId);
		
		int articlesCount = articleService.getArticlesCount(boardId, searchKeywordTypeCode, searchKeyword);
		
		int itemsInAPage = 10;
		int limitFrom = (page - 1) * itemsInAPage;
		int pagesCount = (int) Math.ceil((double) articlesCount / itemsInAPage);	
		
		List<Article> articles;
		
		articles = articleService.getArticles(boardId, limitFrom, itemsInAPage, searchKeywordTypeCode, searchKeyword);
		
		if(board == null && boardId != 0) {
			model.addAttribute("historyBack", true);
			model.addAttribute("msg","존재하지 않는 게시판 입니다.");
			return "usr/common/js";
		}

		model.addAttribute("board", board);
		model.addAttribute("boardId", boardId);
		model.addAttribute("articles", articles);
		model.addAttribute("articlesCount", articlesCount);
		model.addAttribute("page", page);
		model.addAttribute("pagesCount",pagesCount);
		model.addAttribute("searchKeywordTypeCode",searchKeywordTypeCode);
		model.addAttribute("searchKeyword",searchKeyword);

		// ResultData.from("S-1", "게시글 목록을 조회합니다.","articles", articles);
		return "usr/article/list";
	}

	@RequestMapping("/usr/article/detail")
	public String showDetail(Model model, int id) {
		
		articleService.increaseHitCount(id);
		
		Article article = articleService.getArticle(id);

		if (article == null) {
			// ResultData.from("F-1", id + "번글은 존재하지 않습니다.");
			model.addAttribute("historyBack", true);
			model.addAttribute("msg", id + "번글은 존재하지 않습니다.");
			return "usr/common/js";
		}
		// ResultData.from("S-1", id+"번글을 조회합니다.","String", sb.toString());
		
		model.addAttribute("article", article);

		return "usr/article/detail";
	}

	@RequestMapping("/usr/article/write")
	public String showWrite(Model model, int boardId) {
		
		int loginedMemberId = rq.getLoginedMemberId();

		model.addAttribute("loginedMemberId", loginedMemberId);
		model.addAttribute("boardId", boardId);

		// ResultData.newData(writeArticleRd, "String", sb.toString());

		return "usr/article/write";
	}

	@RequestMapping("/usr/article/doWrite")
	@ResponseBody
	public String doWrite(String title, String body, int boardId) {

		if (Ut.empty(title)) {
			// ResultData.from("F-1", "제목을 입력해주세요");
			return Ut.jsHistoryBack("F-1", "제목을 입력해주세요");
		}
		if (Ut.empty(body)) {
			// ResultData.from("F-2", "내용을 입력해주세요");
			return Ut.jsHistoryBack("F-2", "내용을 입력해주세요");
		}

		int loginedMemberId = rq.getLoginedMemberId();

		ResultData<Integer> writeArticleRd = articleService.writeArticle(title, body, loginedMemberId, boardId);

		int id = (int) writeArticleRd.getData1();

		Article article = articleService.getArticle(id);

		// ResultData.newData(writeArticleRd, "String", sb.toString());

		return Ut.jsReplace("S-1", id + "번글이 작성되었습니다.", "detail?id="+id);
	}

	@RequestMapping("/usr/article/modify")
	public String showModify(Model model, int id) {

		Article article = articleService.getArticle(id);

		if (article == null) {
			// ResultData.from("F-1", id + "번글은 존재하지 않습니다.");
			model.addAttribute("historyBack", true);
			model.addAttribute("msg", id + "번글은 존재하지 않습니다.");
			return "usr/common/js";
		}

		int loginedMemberId = rq.getLoginedMemberId();
		
		if (article.getMemberId() != loginedMemberId) {
			// ResultData.from("F-2", Ut.f("해당 글에 대한 권한이 없습니다."));
			model.addAttribute("historyBack", true);
			model.addAttribute("msg", "해당 글에 대한 권한이 없습니다.");
			return "usr/common/js";
		}

		model.addAttribute("article", article);

		return "usr/article/modify";
	}

	@RequestMapping("/usr/article/doModify")
	@ResponseBody
	public String doModify(int id, String title, String body) {
		
		Article article = articleService.getArticle(id);
		
		if (article == null) {
			return Ut.jsHistoryBack("F-1", id + "번글은 존재하지 않습니다.");
		}

		int loginedMemberId = rq.getLoginedMemberId();
		
		if (article.getMemberId() != loginedMemberId) {
			return Ut.jsHistoryBack("F-2", "해당 글에 대한 권한이 없습니다.");
		}
		
		articleService.doModifyArticle(id, title, body);
		
		//ResultData.from("S-1", id + "번글이 수정되었습니다.", "article", article);
		return Ut.jsReplace("S-1", id + "번글이 수정되었습니다.", "detail?id="+id);
	}

	@RequestMapping("/usr/article/doDelete")
	@ResponseBody
	public String doDelete(int id) {

		Article article = articleService.getArticle(id);

		if (article == null) {
			return Ut.jsHistoryBack("F-1", id + "번글은 존재하지 않습니다.");
		}

		int loginedMemberId = rq.getLoginedMemberId();
		if (article.getMemberId() != loginedMemberId) {
			return Ut.jsHistoryBack("F-2", "해당 글에 대한 권한이 없습니다.");
		}

		articleService.doDeleteArticle(article);

		return Ut.jsReplace("S-1", id + "번글이 삭제되었습니다.", "list");
	}
}