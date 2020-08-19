package com.springbootdemo.model.entity;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="verification")
public class VerificationToken {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@Column(name="token")
	private String token;
	
	@OneToOne(targetEntity=SiteUser.class)
	@JoinColumn(name="user_id", nullable=false)
	private SiteUser user;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dateExpires")
	private Date dateExpires;
	
	@Enumerated(EnumType.STRING)
	@Column(name="token_type")
	private TokenType type;
	
	@PrePersist
	private void setDateExpires()
	{
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR, 72);
		dateExpires = c.getTime();
	}

	public VerificationToken() {
		
	}
	
	public VerificationToken(String token, SiteUser user, TokenType type) {
		this.token = token;
		this.user = user;
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public SiteUser getUser() {
		return user;
	}

	public void setUser(SiteUser user) {
		this.user = user;
	}

	public Date getDateExpires() {
		return dateExpires;
	}

	public void setDateExpires(Date dateExpires) {
		this.dateExpires = dateExpires;
	}

	public TokenType getType() {
		return type;
	}

	public void setType(TokenType type) {
		this.type = type;
	}
}
