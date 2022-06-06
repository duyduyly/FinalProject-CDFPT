package com.bigdeal.form;

import javax.validation.constraints.NotEmpty;

import com.bigdeal.entity.Customers;

public class CustomerForm2 {
	private Long id;
	
	@NotEmpty(message = "please your enter customerName")
	private String customerName;
	
	@NotEmpty(message = "please your enter email")
	private String email;
	
	@NotEmpty(message = "please your enter phoneNumber")
	private String phoneNumber;
	
	@NotEmpty(message = "please your enter address")
	private String address;
	
	
	private String gender;
	private int wardId;
	private int districtId;
	private int cityId;

	private boolean newMode = false;

	

	public CustomerForm2() {
		this.newMode = true;
	}

	public CustomerForm2(Customers item) {
		this.id = item.getId();
		this.customerName = item.getCustomerName();
		this.email = item.getEmail();
		this.phoneNumber = item.getPhoneNumber();
		this.address = item.getAddress();
		this.gender = item.getGender();
		this.wardId = item.getWardId();
		this.districtId = item.getDistrictId();
		this.cityId = item.getCityId();
	}

	

	public boolean isNewMode() {
		return newMode;
	}

	public void setNewMode(boolean newMode) {
		this.newMode = newMode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getWardId() {
		return wardId;
	}

	public void setWardId(int wardId) {
		this.wardId = wardId;
	}

	public int getDistrictId() {
		return districtId;
	}

	public void setDistrictId(int districtId) {
		this.districtId = districtId;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}


}