package com.bigdeal.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.bigdeal.entity.Banner;

public class BannerForm {
	private Long id;

	@NotEmpty(message = "please your enter content")
	private String content;
	@NotNull
	private int categoryId;
	@NotNull
	private int position;

	private boolean newMode = false;

	// Upload file.
	private MultipartFile fileData;
	private MultipartFile fileData2;

	public BannerForm() {
		this.newMode = true;
	}

	public BannerForm(Banner item) {
		this.id = item.getId();
		this.content = item.getContent();
		this.categoryId = item.getCategoryId();
		this.position = item.getPosition();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
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

	public MultipartFile getFileData2() {
		return fileData2;
	}

	public void setFileData2(MultipartFile fileData2) {
		this.fileData2 = fileData2;
	}

}