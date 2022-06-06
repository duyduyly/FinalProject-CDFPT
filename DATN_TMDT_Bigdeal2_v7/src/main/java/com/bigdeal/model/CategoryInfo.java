package com.bigdeal.model;

import java.util.Date;

import javax.validation.constraints.NotEmpty;

public class CategoryInfo {

	private Long id;
	@NotEmpty(message = "please your enter categoryname")
	private String categoryName;

	private byte[] image;
	@NotEmpty(message = "please your enter desscription category")
	private String description;
	private Date deletedAt;
	private Date createdAt;
	private Date updatedAt;

	public CategoryInfo() {
	}

	public CategoryInfo(String categoryName) {
		this.categoryName = categoryName;
	}

	public CategoryInfo(String categoryName, byte[] image, String description, Date deletedAt, Date createdAt,
			Date updatedAt) {
		this.categoryName = categoryName;
		this.image = image;
		this.description = description;
		this.deletedAt = deletedAt;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public CategoryInfo(Long id, String categoryName, String description) {
		this.id = id;
		this.categoryName = categoryName;
		this.description = description;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategoryName() {
		return this.categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public byte[] getImage() {
		return this.image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDeletedAt() {
		return this.deletedAt;
	}

	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
}
