package com.bigdeal.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Products")
public class Product implements Serializable {

	private static final long serialVersionUID = -1000119078147252957L;

	@Id
	@Column(name = "CODE", length = 20, nullable = false)
	public String code;

	@Column(name = "Name", length = 255, nullable = false)
	private String name;

	@Column(name = "Price", nullable = false)
	private double price;

	@Column(name = "category_id")
	private Long categoryId;

	@Column(name = "brand_id")
	private Long brandId;
	
	@Column(name = "discount")
	private int discount;
	
	@Column(name = "amount")
	private int amount;
	
	@Column(name = "discription")
	private String discription;
	
	@Column(name = "sort_discription")
	private String sort_discription;
	
	@Column(name = "STATUS")
	private String status;
	
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
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

	@Lob
	@Column(name = "Image", length = Integer.MAX_VALUE, nullable = true)
	private byte[] image;
	
	@Lob
	@Column(name = "Image2", length = Integer.MAX_VALUE, nullable = true)
	private byte[] image2;
	
	@Lob
	@Column(name = "Image3", length = Integer.MAX_VALUE, nullable = true)
	private byte[] image3;
	
	@Lob
	@Column(name = "Image4", length = Integer.MAX_VALUE, nullable = true)
	private byte[] image4;

	public byte[] getImage2() {
		return image2;
	}

	public void setImage2(byte[] image2) {
		this.image2 = image2;
	}

	public byte[] getImage3() {
		return image3;
	}

	public void setImage3(byte[] image3) {
		this.image3 = image3;
	}

	public byte[] getImage4() {
		return image4;
	}

	public void setImage4(byte[] image4) {
		this.image4 = image4;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "Create_Date", nullable = false)
	private Date createDate;
	
	
	
	public Product() {
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
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
//	 @OneToMany(mappedBy = "code")
//		public List<ProductDetail> items = new ArrayList<ProductDetail>();
//
//
//
//	public List<ProductDetail> getItems() {
//		return items;
//	}
//
//	public void setItems(List<ProductDetail> items) {
//		this.items = items;
//	}
	
	
	 
	
}