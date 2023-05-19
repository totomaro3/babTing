package com.babTing.toto.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.babTing.toto.demo.repository.ReactionPointRepository;
import com.babTing.toto.demo.vo.ResultData;

@Service
public class ReactionPointService {

	@Autowired
	private ReactionPointRepository reactionPointRepository;
	@Autowired
	private ArticleService articleService;

	/**
	 * 사용자가 좋아요나 싫어요 버튼을 누를 수 있나?
	 * @param actorId
	 * @param relTypeCode
	 * @param relId
	 * @return
	 */
	public boolean actorCanMakeReaction(int actorId, String relTypeCode, int relId) {
		if (actorId == 0) {
			return false;
		}
		return reactionPointRepository.getSumReactionPointByMemberId(actorId, relTypeCode, relId) == 0;
	}

	/**
	 * 좋아요 증가
	 * @param actorId
	 * @param relTypeCode
	 * @param relId
	 * @return
	 */
	public ResultData addGoodReactionPoint(int actorId, String relTypeCode, int relId) {
		int affectedRow = reactionPointRepository.addGoodReactionPoint(actorId, relTypeCode, relId);

		if (affectedRow != 1) {
			return ResultData.from("F-2", "좋아요 실패");
		}

		switch (relTypeCode) {
		case "article":
			articleService.increaseGoodReactionPoint(relId);
			break;
		}

		return ResultData.from("S-1", "좋아요 처리 됨");

	}

	/**
	 * 싫어요 증가
	 * @param actorId
	 * @param relTypeCode
	 * @param relId
	 * @return
	 */
	public ResultData addBadReactionPoint(int actorId, String relTypeCode, int relId) {
		reactionPointRepository.addBadReactionPoint(actorId, relTypeCode, relId);

		switch (relTypeCode) {
		case "article":
			articleService.increaseBadReactionPoint(relId);
			break;
		}

		return ResultData.from("S-1", "싫어요 처리 됨");
	}

	/**
	 * 사용자가 좋아요를 이미 눌렀나?
	 * @param actorId
	 * @param relTypeCode
	 * @param relId
	 * @return
	 */
	public boolean actorHasGoodReaction(int actorId, String relTypeCode, int relId) {
		if (actorId == 0) {
			return false;
		}
		return reactionPointRepository.actorHasGoodReaction(actorId, relTypeCode, relId);
	}

	/**
	 * 좋아요 감소
	 * @param actorId
	 * @param relTypeCode
	 * @param relId
	 */
	public void delGoodReactionPoint(int actorId, String relTypeCode, int relId) {
		
		switch (relTypeCode) {
		case "article":
			articleService.decreaseGoodReationPoint(relId);
			break;
		}
		reactionPointRepository.delGoodReactionPoint(actorId, relTypeCode, relId);
	}
	
	/**
	 * 사용자가 싫어요를 이미 눌렀나?
	 * @param actorId
	 * @param relTypeCode
	 * @param relId
	 * @return
	 */
	public boolean actorHasBadReaction(int actorId, String relTypeCode, int relId) {
		if (actorId == 0) {
			return false;
		}
		return reactionPointRepository.actorHasBadReaction(actorId, relTypeCode, relId);
	}

	/**
	 * 싫어요 감소
	 * @param actorId
	 * @param relTypeCode
	 * @param relId
	 */
	public void delBadReactionPoint(int actorId, String relTypeCode, int relId) {
		
		switch (relTypeCode) {
		case "article":
			articleService.decreaseBadReationPoint(relId);
			break;
		}
		reactionPointRepository.delBadReactionPoint(actorId, relTypeCode, relId);
	}

}