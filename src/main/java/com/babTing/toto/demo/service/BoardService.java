package com.babTing.toto.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.babTing.toto.demo.repository.BoardRepository;
import com.babTing.toto.demo.vo.Board;

@Service
public class BoardService {
	
	@Autowired
	private BoardRepository boardRepository;

	/**
	 * 게시판 ID로 게시판 전체 가져오기
	 * @param boardId
	 * @return
	 */
	public Board getBoardById(int boardId) {
		return boardRepository.getBoardById(boardId);
	}

}