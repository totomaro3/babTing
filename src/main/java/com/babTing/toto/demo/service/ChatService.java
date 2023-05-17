package com.babTing.toto.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.babTing.toto.demo.repository.ChatRepository;
import com.babTing.toto.demo.vo.Room;

@Service
public class ChatService {
	
	@Autowired
	ChatRepository chatRepository;

	public List<Room> getRooms() {
		return chatRepository.getRooms();
	}

}