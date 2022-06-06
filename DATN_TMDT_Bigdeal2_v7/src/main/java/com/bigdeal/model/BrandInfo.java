package com.bigdeal.model;

import java.util.Date;

public class BrandInfo {

	private Long id;
	private String brandName;
	private byte[] image;
	private String description;
	private Date createdAt;
	private Date updatedAt;

	public BrandInfo() {
	}

	public BrandInfo(String brandName) {
		this.brandName = brandName;
	}

	public BrandInfo(String brandName, byte[] image, String description, Date deletedAt, Date createdAt,
			Date updatedAt) {
		this.brandName = brandName;
		this.image = image;
		this.description = description;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public BrandInfo(Long id, String brandName, String description) {
		this.id = id;
		this.brandName = brandName;
		this.description = description;
	}

	public Long getId() {
		return this.id;
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
