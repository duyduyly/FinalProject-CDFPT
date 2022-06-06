package com.bigdeal.form;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import com.bigdeal.entity.Blogs;
import com.bigdeal.entity.Contact;

public class ContactForm {

	private Long id;
	
	@NotEmpty(message = "please your enter name")
	private String name;
	
	@NotEmpty(message = "please your enter email")
	private String email;
	
	@NotEmpty(message = "please your enter phoneNumber")
	private String phoneNumber;
	
	@NotEmpty(message = "please your enter subject")
	private String subject;
	
	@NotEmpty(message = "please your enter message")
	private String message;

	private boolean newMode = false;


	public ContactForm() {
		this.newMode = true;
	}

	public ContactForm(Contact item) {
		this.id = item.getId();
		this.name = item.getName();
		this.email = item.getEmail();
		this.phoneNumber = item.getPhoneNumber();
		this.subject = item.getSubject();
		this.message = item.getMessage();
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	

}