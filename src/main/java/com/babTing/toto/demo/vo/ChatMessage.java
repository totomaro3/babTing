package com.babTing.toto.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

	private String message;
	private String userName;

	@Override
	public String toString() {
		return "ChatMessage [message =" + message + ", sessionId =" + userName + "]";
	}
}