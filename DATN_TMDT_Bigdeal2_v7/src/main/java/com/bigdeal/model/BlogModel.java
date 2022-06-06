package com.bigdeal.model;

import java.util.Date;

public class BlogModel {

	private Long id;
	private String title;
	private String shortDescription;
	private String description;
	private byte[] image;

	private Date deletedAt;
	private Date createdAt;
	private Date updatedAt;

	public BlogModel() {
	}

	public BlogModel(String title, String shortDescription, String description, byte[] image, Date deletedAt,
			Date createdAt, Date updatedAt) {
		this.title = title;
		this.shortDescription = shortDescription;
		this.description = description;
		this.image = image;

		this.deletedAt = deletedAt;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public BlogModel(Long id, String title, String shortDescription, String description) {
		this.id = id;
		this.title = title;
		this.shortDescription = shortDescription;
		this.description = description;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
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

}
