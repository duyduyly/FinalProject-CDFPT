package com.bigdeal.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.bigdeal.dao.CategoryDAO;
import com.bigdeal.dao.ProductDAO;
import com.bigdeal.entity.Categories;
import com.bigdeal.entity.Product;
import com.bigdeal.form.CategoryForm;
import com.bigdeal.form.ProductForm;

@Component
public class CategoryFormValidator implements Validator {

	@Autowired
	private CategoryDAO categoryDAO;

	// Validator này chỉ dùng để kiểm tra class ProductForm.
	@Override
	public boolean supports(Class<?> clazz) {
		return clazz == CategoryForm.class;
	}

	@Override
	public void validate(Object target, Errors errors) {
		CategoryForm form = (CategoryForm) target;

		// Kiểm tra các trường (field) của ProductForm.
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "categoryName", "NotEmpty.productForm.name");

	}

}