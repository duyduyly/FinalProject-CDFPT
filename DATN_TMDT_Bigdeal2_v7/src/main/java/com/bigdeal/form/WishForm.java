package com.bigdeal.form;

import com.bigdeal.entity.Wish;

public class WishForm {
	private Long id;
	private String userName;
	private String productCode;

	private boolean newMode = false;

	// Upload file.


	public WishForm() {
		this.newMode = true;
	}

	public WishForm(Wish item) {
		this.id = item.getId();
		this.userName = item.getUserName();
		this.productCode = item.getProductCode();

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public boolean isNewMode() {
		return newMode;
	}

	public void setNewMode(boolean newMode) {
		this.newMode = newMode;
	}



}