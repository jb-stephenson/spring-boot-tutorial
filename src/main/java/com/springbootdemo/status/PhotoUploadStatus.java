package com.springbootdemo.status;

public class PhotoUploadStatus {
	private String message;
	
	public PhotoUploadStatus(String message)	{
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
}
