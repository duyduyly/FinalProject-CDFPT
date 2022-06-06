package com.bigdeal.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.bigdeal.entity.Account;

// lớp form này dùng để lưu dự liệu trên form font end vừa nhập để set các dữ liệu vào ........

public class AccountForm {
	
	@NotEmpty(message = "please your enter username")
	private String userName;
	
	private String userRole;
	@NotEmpty(message = "please your enter password")
	@Size(min = 8, max = 16, message = "độ dài mật khẩu nằm trong khoảng từ 8 đến 16")
	private String password;
	private boolean active;
	
	private String email;
	private MultipartFile fileAvatar;
	private boolean newMode = false;

	public AccountForm() {
		this.newMode = true;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public AccountForm(Account item) {

		this.userName = item.getUserName();
		this.email = item.getEmail();
		this.userRole = item.getUserRole();
		this.active = item.isActive();
		this.password = item.getEncrytedPassword();
	}

	

	
	public MultipartFile getFileAvatar() {
		return fileAvatar;
	}

	public void setFileAvatar(MultipartFile fileAvatar) {
		this.fileAvatar = fileAvatar;
	}

	public boolean isNewMode() {
		return newMode;
	}

	public void setNewMode(boolean newMode) {
		this.newMode = newMode;
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}