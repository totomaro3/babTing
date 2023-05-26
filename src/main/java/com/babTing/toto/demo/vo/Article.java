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
	private int boardId;
	private int MemberId;
	private int hitCount;
	private int goodReactionPoint;
	private int badReactionPoint;
	private String restaurantName;
	private String address;
	private double longitude;
	private double latitude;
	private int distance;
	private int deliveryCost;
	private String deadlineTime;
	private int deadStatus;
	private int Participants;
	
	private String extra__writer;
	private String extra__writerLongitude;
	private String extra__writerLatitude;
	private int extra__participants;
}
