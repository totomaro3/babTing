package com.babTing.toto.demo.vo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reply {
	private int id;
	private String regDate;
	private String updateDate;
	private String body;
	private int MemberId;
	private String relTypeCode;
	private int relId;
	private int goodReactionPoint;
	private int badReactionPoint;
	
	private String extra__writer;
}