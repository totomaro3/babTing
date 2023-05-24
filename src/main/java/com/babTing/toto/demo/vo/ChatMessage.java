package com.babTing.toto.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
	
	private int id;
	private String regDate;
	private String updateDate;
	private String type;
	private String message;
	private String userName;
	private int relId;

	/*
	@Override
	public String toString() {
		return "ChatMessage [message =" + message + ", sessionId =" + userName + "]";
	}
	*/
}