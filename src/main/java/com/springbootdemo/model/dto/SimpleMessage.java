package com.springbootdemo.model.dto;

import java.util.Date;

import com.springbootdemo.model.entity.Message;

public class SimpleMessage {
	
	private Long id;
	
	private String from;
	
	private String text;
	
	private Date sent;
	
	private Long fromUserId;
	
	private boolean isReply;
	
	public SimpleMessage() {
		
	}
	
	public SimpleMessage(String text) {
		this.text = text;
		this.sent = new Date();
	}
	
	public SimpleMessage(Message m, Boolean isReply) {
		this.from = m.getFromUser().getFirstname() + " " + m.getFromUser().getSurname();
		this.fromUserId = m.getFromUser().getId();
		this.sent = m.getSent();
		this.text = m.getText();
		this.isReply = isReply;
		this.id = m.getId();
	}
	
	public SimpleMessage(String from, String text, Date sent, Long fromUserId) {
		this.from = from;
		this.text = text;
		this.sent = sent;
		this.fromUserId = fromUserId;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getSent() {
		return sent;
	}

	public void setSent(Date sent) {
		this.sent = sent;
	}

	public Long getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(Long fromUserId) {
		this.fromUserId = fromUserId;
	}

	public boolean isReply() {
		return isReply;
	}

	public void setIsReply(boolean isReply) {
		this.isReply = isReply;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "SimpleMessage [from=" + from + ", text=" + text + ", sent=" + sent + ", fromUserId=" + fromUserId
				+ ", isReply=" + isReply + "]";
	}
	
}
