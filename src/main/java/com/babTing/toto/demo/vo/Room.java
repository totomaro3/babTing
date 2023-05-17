package com.babTing.toto.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {
	
	private int roomNumber;
	private String roomName;
	
	@Override
	public String toString() {
		return "Room [roomNumber=" + roomNumber + ", roomName=" + roomName + "]";
	}
}