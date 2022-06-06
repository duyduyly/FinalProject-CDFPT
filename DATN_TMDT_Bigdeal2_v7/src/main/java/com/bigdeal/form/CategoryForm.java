package com.bigdeal.form;

import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import com.bigdeal.entity.Categories;

public class CategoryForm {
	private Long id;
	
	@NotEmpty(message = "please your enter categoryName")
	private String categoryName;
	
	@NotEmpty(message = "please your enter description")
	private String description;

	private boolean newMode = false;

	// Upload file.
	private MultipartFile fileData;

	public CategoryForm() {
		this.newMode = true;
	}

	public CategoryForm(Categories category) {
		this.id = category.getId();
		this.categoryName = category.getCategoryName();
		this.description = category.getDescription();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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