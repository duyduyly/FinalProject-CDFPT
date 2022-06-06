package com.bigdeal.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.bigdeal.model.CustomerInfo;

public class CustomerForm {
	 
	@NotEmpty(message = "please your enter name")
    private String name;
	
	@NotEmpty(message = "please your enter address")
    private String address;
	
    @NotEmpty(message = "please your enter email")
    @Email
    private String email;
	
	@NotEmpty(message = "please your enter phone")
    private String phone;
 
    private boolean valid;
 
    public CustomerForm() {
 
    }
 
    public CustomerForm(CustomerInfo customerInfo) {
        if (customerInfo != null) {
            this.name = customerInfo.getName();
            this.address = customerInfo.getAddress();
            this.email = customerInfo.getEmail();
            this.phone = customerInfo.getPhone();
        }
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
 
    public String getAddress() {
        return address;
    }
 
    public void setAddress(String address) {
        this.address = address;
    }
 
    public String getPhone() {
        return phone;
    }
 
    public void setPhone(String phone) {
        this.phone = phone;
    }
 
    public boolean isValid() {
        return valid;
    }
 
    public void setValid(boolean valid) {
        this.valid = valid;
    }
 
}