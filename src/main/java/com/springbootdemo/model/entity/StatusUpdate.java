package com.springbootdemo.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="status_update")
public class StatusUpdate 
{	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	
	@Size(min=5, max=255, message="{addstatus.text.size}")
	@Column(name="text")
	private String text;
	
	@Column(name="dateAdded")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="yyyy/MM/dd hh:mm:ss")
	private Date dateAdded;
	
	@PrePersist
	protected void onCreate() {
		if(dateAdded == null) {
			dateAdded = new Date();
		}
	}
	
	public StatusUpdate() {
		
	}
	
	public StatusUpdate(String text) {
		this.text = text;
	}
	
	public StatusUpdate(String text, Date dateAdded) {
		this.text = text;
		this.dateAdded = dateAdded;
	}

	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getDateAdded() {
		return dateAdded;
	}
	
	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateAdded == null) ? 0 : dateAdded.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StatusUpdate other = (StatusUpdate) obj;
		if (dateAdded == null) {
			if (other.dateAdded != null)
				return false;
		} else if (!dateAdded.equals(other.dateAdded))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "StatusUpdate [id=" + id + ", text=" + text + ", dateAdded=" + dateAdded + "]";
	}
}
