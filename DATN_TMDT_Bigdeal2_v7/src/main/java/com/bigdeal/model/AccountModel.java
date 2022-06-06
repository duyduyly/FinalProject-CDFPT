package com.bigdeal.model;

import org.springframework.web.multipart.MultipartFile;

// Lớp model dùng để chứa thông tin dữ liệu liên kết 2 bảng trong MySQL
// Nếu để 1 lớp entity thì viết câu lệnh truy vấn ko thể chứa hết 
// dữ liệu nên thông qua lớp model để truy vấn câu lệnh MySQL hibernate 
public class AccountModel {

	private String userName;
	private String userRole;
	
	private String email;
	
	private String password;
	private boolean active;
	
	private MultipartFile fileAvatar;
	
	public MultipartFile getFileAvatar() {
		return fileAvatar;
	}

	public void setFileAvatar(MultipartFile fileAvatar) {
		this.fileAvatar = fileAvatar;
	}

	public AccountModel() {
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
	public AccountModel(String userName, String userRole, boolean active, String email) {
		this.userName = userName;
		this.userRole = userRole;
		this.active = active;
		this.email = email;
	}

	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	

}
