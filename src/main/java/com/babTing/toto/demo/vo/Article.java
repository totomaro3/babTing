package com.babTing.toto.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {
	private int id;
	private String regDate;
	private String updateDate;
	private String title;
	private String body;
	private String boardId;
	private int MemberId;
	private int hitCount;
	private String restaurantName;
	private int distance;
	private int deliveryCost;
	private String deadlineTime;
	private int Participants;
	
	private String extra__writer;
}
