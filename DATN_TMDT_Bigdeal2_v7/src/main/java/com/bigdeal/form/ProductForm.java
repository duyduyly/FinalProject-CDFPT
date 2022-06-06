package com.bigdeal.form;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import com.bigdeal.entity.Product;

public class ProductForm {
	
	@NotEmpty(message = "please your enter code")
	private String code;
	
	@NotEmpty(message = "please your enter name")
	private String name;
	
	private double price;
	private Long categoryId;
	private Long brandId;
	private int discount;
	private boolean newProduct = false;
	private int amount;
	
	private String status;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@NotEmpty(message = "please your enter discription")
	private String discription;
	
	@NotEmpty(message = "please your enter sort discription")
	private String sort_discription;

	// Upload file.
	private MultipartFile fileData;
	
	private MultipartFile fileimage2;
	
	private MultipartFile fileimage3;
	
	private MultipartFile fileimage4;
	

	public MultipartFile getFileimage2() {
		return fileimage2;
	}

	public void setFileimage2(MultipartFile fileimage2) {
		this.fileimage2 = fileimage2;
	}

	public MultipartFile getFileimage3() {
		return fileimage3;
	}

	public void setFileimage3(MultipartFile fileimage3) {
		this.fileimage3 = fileimage3;
	}

	public MultipartFile getFileimage4() {
		return fileimage4;
	}

	public void setFileimage4(MultipartFile fileimage4) {
		this.fileimage4 = fileimage4;
	}

	public ProductForm() {
		this.newProduct = true;
	}

	public ProductForm(Product product) {
		this.code = product.getCode();
		this.name = product.getName();
		this.price = product.getPrice();
		this.categoryId = product.getCategoryId();
		this.brandId = product.getBrandId();
		this.discount = product.getDiscount();
		this.amount = product.getAmount();
		this.discription = product.getDiscription();
		this.sort_discription = product.getSort_discription();
		this.status = product.getStatus();
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

	public MultipartFile getFileData() {
		return fileData;
	}

	public void setFileData(MultipartFile fileData) {
		this.fileData = fileData;
	}

	public boolean isNewProduct() {
		return newProduct;
	}

	public void setNewProduct(boolean newProduct) {
		this.newProduct = newProduct;
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

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
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

}