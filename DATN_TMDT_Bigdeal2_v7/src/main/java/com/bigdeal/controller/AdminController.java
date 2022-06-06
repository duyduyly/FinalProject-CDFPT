package com.bigdeal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bigdeal.dao.OrderDAO;
import com.bigdeal.form.ProductForm;
import com.bigdeal.model.OrderDetailInfo;
import com.bigdeal.model.OrderInfo;
import com.bigdeal.pagination.PaginationResult;
import com.bigdeal.validator.ProductFormValidator;

@Controller
@Transactional
public class AdminController {

	@Autowired
	private OrderDAO orderDAO;


	@Autowired
	private ProductFormValidator productFormValidator;

	@InitBinder
	public void myInitBinder(WebDataBinder dataBinder) {
		Object target = dataBinder.getTarget();
		if (target == null) {
			return;
		}
		System.out.println("Target=" + target);

		if (target.getClass() == ProductForm.class) {
			dataBinder.setValidator(productFormValidator);
		}
		
	}

	// GET: Hiển thị trang login
	@RequestMapping(value = { "/ad/login" }, method = RequestMethod.GET)
	public String login(Model model) {

		return "login";
	}

	@RequestMapping(value = { "/admin/accountInfo" }, method = RequestMethod.GET)
	public String accountInfo(Model model) {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		System.out.println(userDetails.getPassword());
		System.out.println(userDetails.getUsername());
		System.out.println(userDetails.isEnabled());

		model.addAttribute("userDetails", userDetails);
		return "accountInfo";
	}

	

	


	

}