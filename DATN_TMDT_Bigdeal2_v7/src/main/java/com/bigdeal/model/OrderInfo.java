package com.bigdeal.model;

import java.util.Date;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

public class OrderInfo {

	private String id;
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private Date orderDate;
	private int orderNum;
	private double amount;
	@NotEmpty(message = "please your enter cutomername")
	private String customerName;
	@NotEmpty(message = "please your enter cutomer address")
	private String customerAddress;
	@NotEmpty(message = "please your enter cutomer email")
	@Email(regexp = "", message = "")
	private String customerEmail;
	@NotEmpty(message = "please your enter cutomer phone")
	@Pattern(regexp = "", message = "")
	private String customerPhone;
	private String userName;
	private int status;
	private List<OrderDetailInfo> details;

	public OrderInfo() {

	}

	// Sử dụng cho Hibernate Query.
	public OrderInfo(String id, Date orderDate, int orderNum, //
			double amount, String customerName, String customerAddress, //
			String customerEmail, String customerPhone, String userName, int status) {
		this.id = id;
		this.orderDate = orderDate;
		this.orderNum = orderNum;
		this.amount = amount;

		this.customerName = customerName;
		this.customerAddress = customerAddress;
		this.customerEmail = customerEmail;
		this.customerPhone = customerPhone;
		this.userName = userName;
		this.status = status;
	}

	public OrderInfo(int orderDate, double amount) {
		this.orderNum = orderDate;
		this.amount = amount;
	}

	public OrderInfo(Date orderDate, double amount) {
		this.orderDate = orderDate;
		this.amount = amount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public List<OrderDetailInfo> getDetails() {
		return details;
	}

	public void setDetails(List<OrderDetailInfo> details) {
		this.details = details;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}