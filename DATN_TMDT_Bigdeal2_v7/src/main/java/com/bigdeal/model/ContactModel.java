package com.bigdeal.model;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class ContactModel {

	private Long id;
	@NotEmpty(message = "please your enter name")
	private String name;
	@NotEmpty(message = "please your enter Email")
	@Email
	private String email;
	@NotEmpty(message = "")
	@Pattern(regexp = "",message = "")
	private String phoneNumber;
	@NotEmpty(message = "please your enter Subject")
	private String subject;
	@NotEmpty(message = "please your enter message")
	private String message;

	public ContactModel() {
	}

	public ContactModel(Long id, String name, String email, String phoneNumber, String subject, String message) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.subject = subject;
		this.message = message;
	}

	public Long getId() {
		return this.id;
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
