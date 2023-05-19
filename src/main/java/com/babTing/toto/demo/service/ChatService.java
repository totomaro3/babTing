package com.babTing.toto.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.babTing.toto.demo.repository.ChatRepository;
import com.babTing.toto.demo.vo.ChatMessage;
import com.babTing.toto.demo.vo.Room;

@Service
public class ChatService {
	
	@Autowired
	ChatRepository chatRepository;

	/**
	 * 방 불러오기
	 * @return
	 */
	public List<Room> getRooms() {
		return chatRepository.getRooms();
	}

	/**
	 * 채팅 메시지를 데이터베이스에 저장하는 로직을 수행합니다.
	 * @param message
	 * @param userName
	 * @param relId
	 */
	public void saveChatMessage(String message, String userName, int relId) {
        // 채팅 메시지를 데이터베이스에 저장하는 로직을 수행합니다.
        chatRepository.save(message, userName, relId);
    }

	/**
	 * 채팅 메시지를 데이터베이스에서 불러오는 로직을 수행합니다.
	 * @param relId
	 * @return
	 */
	public List<ChatMessage> getChatMessages(int relId) {
		List<ChatMessage> chatMessages = chatRepository.load(relId);
		return chatMessages;
	}
}