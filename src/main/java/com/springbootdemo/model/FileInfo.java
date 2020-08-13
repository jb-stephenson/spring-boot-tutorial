package com.springbootdemo.model;

public class FileInfo {
	
	private String baseName;
	
	private String ext;
	
	private String subDirectory;
	
	private String baseDirectory;

	public FileInfo(String baseName, String ext, String subDirectory, String baseDirectory) {
		this.baseName = baseName;
		this.ext = ext;
		this.subDirectory = subDirectory;
		this.baseDirectory = baseDirectory;
	}

	public String getBaseName() {
		return baseName;
	}

	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getSubDirectory() {
		return subDirectory;
	}

	public void setSubDirectory(String subDirectory) {
		this.subDirectory = subDirectory;
	}

	public String getBaseDirectory() {
		return baseDirectory;
	}

	public void setBaseDirectory(String baseDirectory) {
		this.baseDirectory = baseDirectory;
	}

	@Override
	public String toString() {
		return "FileInfo [baseName=" + baseName + ", ext=" + ext + ", subDirectory=" + subDirectory + ", baseDirectory="
				+ baseDirectory + "]";
	}
}
