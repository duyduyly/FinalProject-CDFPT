package com.bigdeal.model;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.lang.NonNull;

public class CustomerModel {

	private Long id;
	@NotEmpty(message = "please your enter customername")
	private String customerName;
	@NotEmpty(message = "please your enter email")
	@Email(regexp = "", message = "")
	private String email;
	@NotEmpty(message = "please your enter phonenumber")
	@Pattern(regexp = "",message = "")
	private String phoneNumber;
	@NotEmpty(message = "please your enter address")
	private String address;
	@NotNull(message = "please your enter gender")
	private String gender;
	private int wardId;
	private int districtId;
	private int cityId;

	private Date deletedAt;
	private Date createdAt;
	private Date updatedAt;

	public CustomerModel() {
	}

//	public CustomerModel(String title, String shortDescription, String description, byte[] image, Date deletedAt,
//			Date createdAt, Date updatedAt) {
//		this.title = title;
//		this.shortDescription = shortDescription;
//		this.description = description;
//		this.image = image;
//
//		this.deletedAt = deletedAt;
//		this.createdAt = createdAt;
//		this.updatedAt = updatedAt;
//	}
//
	public CustomerModel(Long id, String customerName, String email, String phoneNumber, String address, String gender) {
		this.id = id;
		this.customerName = customerName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.gender = gender;
		
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
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
