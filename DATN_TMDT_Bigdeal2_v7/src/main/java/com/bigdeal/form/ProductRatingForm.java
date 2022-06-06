package com.bigdeal.form;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.springframework.web.multipart.MultipartFile;

import com.bigdeal.entity.Product;
import com.bigdeal.entity.ProductRating;

public class ProductRatingForm {

	private Long id;
	private String userName;
	private String productCode;
//	@Column(name = "product_code", length = 50)
//	private String productCode;
	private short rating;
	private String message;
	private boolean newProduct = false;

	public ProductRatingForm() {
		this.newProduct = true;
	}

	public ProductRatingForm(ProductRating productRating) {
		this.userName = productRating.getUserName();
		this.productCode = productRating.getProductCode();
		this.message = productRating.getMessage();
		this.rating = productRating.getRating();

	}

	public boolean isNewProduct() {
		return newProduct;
	}

	public void setNewProduct(boolean newProduct) {
		this.newProduct = newProduct;
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

	public short getRating() {
		return rating;
	}

	public void setRating(short rating) {
		this.rating = rating;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}