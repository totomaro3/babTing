package com.babTing.toto.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.babTing.toto.demo.repository.ReplyRepository;
import com.babTing.toto.demo.util.Ut;
import com.babTing.toto.demo.vo.Reply;
import com.babTing.toto.demo.vo.ResultData;

@Service
public class ReplyService {

	@Autowired
	private ReplyRepository replyRepository;

	/**
	 * 댓글 작성
	 * @param actorId
	 * @param relTypeCode
	 * @param relId
	 * @param body
	 * @return
	 */
	public ResultData<Integer> writeReply(int actorId, String relTypeCode, int relId, String body) {
		replyRepository.writeReply(actorId, relTypeCode, relId, body);

		int id = replyRepository.getLastInsertId();

		return ResultData.from("S-1", Ut.f("%d번 댓글이 생성되었습니다", id), "id", id);
	}
	
	/**
	 * 댓글 전체 불러오기
	 * @param memberId
	 * @param relTypeCode
	 * @param relId
	 * @return
	 */
	public List<Reply> getReplies(int memberId, String relTypeCode, int relId) {
		
		return replyRepository.getReplies(memberId, relTypeCode, relId);
	}

	/**
	 * id로 댓글 한개 불러오기 (수정, 삭제 시)
	 * @param id
	 * @return
	 */
	public ResultData<Reply> getReply(int id) {
		
		Reply reply = replyRepository.getReply(id);
		
		return ResultData.from("S-1", Ut.f("%d번 댓글글을 불러왔습니다.", id),"reply", reply);
	}

	/**
	 * 댓글 수정
	 * @param id
	 * @param body
	 * @return
	 */
	public ResultData<Integer> doModifyReply(int id, String body) {
		
		replyRepository.doModifyReply(id,body);
		
		return ResultData.from("S-1", Ut.f("%d번 댓글이 수정되었습니다", id),"id", id);
	}

	/**
	 * 댓글 삭제
	 * @param id
	 * @return
	 */
	public ResultData<Integer> doDeleteReply(int id) {
		
		replyRepository.doDeleteReply(id);
		
		return ResultData.from("S-1", Ut.f("%d번 댓글이 삭제되었습니다", id),"id", id);
	}
}