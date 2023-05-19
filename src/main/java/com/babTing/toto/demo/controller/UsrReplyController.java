package com.babTing.toto.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.babTing.toto.demo.service.ReplyService;
import com.babTing.toto.demo.util.Ut;
import com.babTing.toto.demo.vo.Reply;
import com.babTing.toto.demo.vo.ResultData;
import com.babTing.toto.demo.vo.Rq;

@Controller
public class UsrReplyController {

	@Autowired
	private Rq rq;
	@Autowired
	private ReplyService replyService;

	/**
	 * 댓글 관련 기능은 자유게시판에서만 사용이 가능합니다!
	 */
	
	/**
	 * 댓글 작성 (INSERT)
	 * @param relTypeCode
	 * @param relId
	 * @param body
	 * @param replaceUri
	 * @return
	 */
	@RequestMapping("/usr/reply/doWrite")
	@ResponseBody
	public String doWrite(String relTypeCode, int relId, String body, String replaceUri) {

		if (Ut.empty(relTypeCode)) {
			return rq.jsHistoryBack("F-1", "relTypeCode 을(를) 입력해주세요");
		}
		if (Ut.empty(relId)) {
			return rq.jsHistoryBack("F-2", "relId 을(를) 입력해주세요");
		}
		if (Ut.empty(body)) {
			return rq.jsHistoryBack("F-3", "body 을(를) 입력해주세요");
		}

		ResultData<Integer> writeReplyRd = replyService.writeReply(rq.getLoginedMemberId(), relTypeCode, relId, body);

		int id = (int) writeReplyRd.getData1();

		if (Ut.empty(replaceUri)) {
			replaceUri = Ut.f("../article/detail?id=%d", relId);
		}

		return rq.jsReplace(writeReplyRd.getMsg(), replaceUri);
	}
	
	/**
	 * 댓글 수정 폼
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping("/usr/reply/modify")
	public String showModify(Model model, int id) {

		ResultData<Reply> getReplyRd = replyService.getReply(id);
		
		Reply reply = getReplyRd.getData1();

		if (reply == null) {
			// ResultData.from("F-1", id + "번글은 존재하지 않습니다.");
			return rq.jsHitoryBackOnView(id + "번 댓글은 존재하지 않습니다.");
		}

		int loginedMemberId = rq.getLoginedMemberId();
		
		if (reply.getMemberId() != loginedMemberId) {
			// ResultData.from("F-2", Ut.f("해당 글에 대한 권한이 없습니다."));
			return rq.jsHitoryBackOnView("해당 글에 대한 권한이 없습니다.");
		}

		model.addAttribute("reply", reply);

		return "usr/reply/modify";
	}
	
	/**
	 * 댓글 수정 (UPDATE)
	 * @param id
	 * @param body
	 * @return
	 */
	@RequestMapping("/usr/reply/doModify")
	@ResponseBody
	public String doModify(int id, String body) {
		
		ResultData<Reply> getReplyRd = replyService.getReply(id);
		
		Reply reply = getReplyRd.getData1();
		
		if (reply == null) {
			// ResultData.from("F-1", id + "번글은 존재하지 않습니다.");
			return rq.jsHitoryBackOnView(id + "번 댓글은 존재하지 않습니다.");
		}
		
		int loginedMemberId = rq.getLoginedMemberId();
		
		if (reply.getMemberId() != loginedMemberId) {
			return Ut.jsHistoryBack("F-2", "해당 글에 대한 권한이 없습니다.");
		}
		
		ResultData<Integer> doModifyReply = replyService.doModifyReply(id, body);
		
		//ResultData.from("S-1", id + "번글이 수정되었습니다.", "article", article);
		return Ut.jsReplace(doModifyReply.getResultCode(),doModifyReply.getMsg(), "../article/detail?id="+reply.getRelId());
	}
	
	/**
	 * 댓글 삭제 (DELETE)
	 * @param id
	 * @return
	 */
	@RequestMapping("/usr/reply/doDelete")
	@ResponseBody
	public String doDelete(int id) {

		ResultData<Reply> getReplyRd = replyService.getReply(id);
		Reply reply = getReplyRd.getData1();

		if (reply == null) {
			return Ut.jsHistoryBack("F-1", id + "번 댓글은 존재하지 않습니다.");
		}

		int loginedMemberId = rq.getLoginedMemberId();
		if (reply.getMemberId() != loginedMemberId) {
			return Ut.jsHistoryBack("F-2", "해당 댓글에 대한 권한이 없습니다.");
		}

		ResultData<Integer> doDeleteReplyRd = replyService.doDeleteReply(id);

		return Ut.jsReplace(doDeleteReplyRd.getResultCode(), doDeleteReplyRd.getMsg(), "../article/detail?id="+reply.getRelId());
	}

}