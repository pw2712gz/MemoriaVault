package com.ayubyusuf.memoriavault.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Photo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String fileName;
	private String fileType;

	@Temporal(TemporalType.TIMESTAMP)
	private Date uploadDate;

	public Photo() {
		// Default constructor for JPA
	}

	public Photo(String fileName, String fileType, Date uploadDate) {
		this.fileName = fileName;
		this.fileType = fileType;
		this.uploadDate = uploadDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
}
