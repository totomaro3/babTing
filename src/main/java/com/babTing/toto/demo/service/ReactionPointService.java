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

	public boolean actorCanMakeReaction(int actorId, String relTypeCode, int relId) {
		if (actorId == 0) {
			return false;
		}
		return reactionPointRepository.getSumReactionPointByMemberId(actorId, relTypeCode, relId) == 0;
	}

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

	public ResultData addBadReactionPoint(int actorId, String relTypeCode, int relId) {
		reactionPointRepository.addBadReactionPoint(actorId, relTypeCode, relId);

		switch (relTypeCode) {
		case "article":
			articleService.increaseBadReactionPoint(relId);
			break;
		}

		return ResultData.from("S-1", "싫어요 처리 됨");
	}

	public boolean actorHasGoodReaction(int actorId, String relTypeCode, int relId) {
		if (actorId == 0) {
			return false;
		}
		return reactionPointRepository.actorHasGoodReaction(actorId, relTypeCode, relId);
	}

	public void delGoodReactionPoint(int actorId, String relTypeCode, int relId) {
		
		switch (relTypeCode) {
		case "article":
			articleService.decreaseGoodReationPoint(relId);
			break;
		}
		reactionPointRepository.delGoodReactionPoint(actorId, relTypeCode, relId);
	}
	
	public boolean actorHasBadReaction(int actorId, String relTypeCode, int relId) {
		if (actorId == 0) {
			return false;
		}
		return reactionPointRepository.actorHasBadReaction(actorId, relTypeCode, relId);
	}
	
	public void delBadReactionPoint(int actorId, String relTypeCode, int relId) {
		
		switch (relTypeCode) {
		case "article":
			articleService.decreaseBadReationPoint(relId);
			break;
		}
		reactionPointRepository.delBadReactionPoint(actorId, relTypeCode, relId);
	}

}