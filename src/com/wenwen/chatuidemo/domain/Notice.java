package com.wenwen.chatuidemo.domain;

public class Notice {
	private int id;
	private String date;
	private String title;
	private String content;
	private String from;
	private String subview;

	public String getSubview() {
		return subview;
	}
	public void setSubview(String subview) {
		this.subview = subview;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	
}
