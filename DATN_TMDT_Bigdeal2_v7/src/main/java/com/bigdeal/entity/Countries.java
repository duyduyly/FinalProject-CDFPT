package com.bigdeal.entity;
// Generated Sep 19, 2020 3:34:09 PM by Hibernate Tools 5.4.18.Final

import java.util.Date;

/**
 * Countries generated by hbm2java
 */
public class Countries implements java.io.Serializable {

	private Long id;
	private String countryName;
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;

	public Countries() {
	}

	public Countries(String countryName) {
		this.countryName = countryName;
	}

	public Countries(String countryName, Date createdAt, Date updatedAt, Date deletedAt) {
		this.countryName = countryName;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.deletedAt = deletedAt;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCountryName() {
		return this.countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
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

	public Date getDeletedAt() {
		return this.deletedAt;
	}

	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}

}
