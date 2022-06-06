package com.bigdeal.form;

import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import com.bigdeal.entity.Brands;
import com.bigdeal.entity.Categories;

public class BrandForm {
	private Long id;
	
	@NotEmpty(message = "please your enter brandName")
	private String brandName;

	@NotEmpty(message = "please your enter description")
	private String description;

	private boolean newMode = false;
	private Categories categoriesId;
	// Upload file.
	private MultipartFile fileData;

	public Categories getCategoriesId() {
		return categoriesId;
	}

	public void setCategoriesId(Categories categoriesId) {
		this.categoriesId = categoriesId;
	}

	public BrandForm() {
		this.newMode = true;
	}

	public BrandForm(Brands category) {
		this.id = category.getId();
		this.brandName = category.getBrandName();
		this.description = category.getDescription();
		this.categoriesId = category.getCategoriesId();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
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