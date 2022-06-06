package com.bigdeal.form;

import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import com.bigdeal.entity.Blogs;

public class BlogForm {
	private Long id;
	
	@NotEmpty(message = "please your enter title")
	private String title;
	
	@NotEmpty(message = "please your enter shortDescription")
	private String shortDescription;
	
	@NotEmpty(message = "please your enter description")
	private String description;

	private boolean newMode = false;

	// Upload file.
	private MultipartFile fileData;

	public BlogForm() {
		this.newMode = true;
	}

	public BlogForm(Blogs item) {
		this.id = item.getId();
		this.title = item.getTitle();
		this.shortDescription = item.getShortDescription();
		this.description = item.getDescription();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isNewMode() {
		return newMode;
	}

	public void setNewMode(boolean newMode) {
		this.newMode = newMode;
	}

	public MultipartFile getFileData() {
		return fileData;
	}

	public void setFileData(MultipartFile fileData) {
		this.fileData = fileData;
	}

}