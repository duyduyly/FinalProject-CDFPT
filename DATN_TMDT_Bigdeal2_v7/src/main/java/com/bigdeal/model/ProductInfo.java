package com.bigdeal.model;

import javax.persistence.Column;

import com.bigdeal.entity.Product;

public class ProductInfo {
	private String code;
	private String name;
	private double price;
	private Long categoryId;
	private Long brandId;
	private String categoryName;
	private String brandName;
	private int discount;
	private int amount;
	private String discription;
	private String sort_discription;
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ProductInfo() {
	}

	public ProductInfo(Product product) {
		this.code = product.getCode();
		this.name = product.getName();
		this.price = product.getPrice();
		this.categoryId = product.getCategoryId();
		this.brandId = product.getBrandId();
		this.amount = product.getAmount();
		this.discription = product.getDiscription();
		this.sort_discription = product.getSort_discription();
		this.status = product.getStatus();
	}

//	// Sử dụng trong JPA/Hibernate query
//	public ProductInfo(String code, String name, double price, Long categoryId, Long brandId, int discount) {
//		this.code = code;
//		this.name = name;
//		this.price = price;
//		this.categoryId = categoryId;
//		this.brandId = brandId;
//		this.discount = discount;
//	}

	public ProductInfo(String code, String name, double price, Long categoryId, Long brandId, int discount, int amount,
			String discription, String sort_discription, String status) {

		this.code = code;
		this.name = name;
		this.price = price;
		this.categoryId = categoryId;
		this.brandId = brandId;
		this.categoryName = categoryName;
		this.brandName = brandName;
		this.discount = discount;
		this.amount = amount;
		this.discription = discription;
		this.sort_discription = sort_discription;
		this.status = status;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	public String getSort_discription() {
		return sort_discription;
	}

	public void setSort_discription(String sort_discription) {
		this.sort_discription = sort_discription;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

}
