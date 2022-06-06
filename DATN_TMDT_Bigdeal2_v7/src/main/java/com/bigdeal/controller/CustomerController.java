package com.bigdeal.controller;

import javax.validation.Valid;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bigdeal.constants.Consts;
import com.bigdeal.dao.CustomerDAO;
import com.bigdeal.entity.Customers;
import com.bigdeal.form.CustomerForm2;
import com.bigdeal.model.CustomerModel;
import com.bigdeal.pagination.PaginationResult;

@Controller
@Transactional
public class CustomerController {

	@Autowired
	private CustomerDAO customerDAO;

	// Danh sách sản phẩm.
	@RequestMapping(value = Consts.adminPath + "/customers", method = RequestMethod.GET)
	public String listProductHandler(Model model, //
			@RequestParam(value = "name", defaultValue = "") String likeName,
			@RequestParam(value = "page", defaultValue = "1") int page) {

		PaginationResult<CustomerModel> result = customerDAO.query(page, //
				Consts.RESULT_PER_PAGE, Consts.MAX_NAVIGATION_PAGE, likeName);

		model.addAttribute("data", result);
		return "customer/index";
	}

//	@RequestMapping(value = { Consts.adminPath + "/customers/image" }, method = RequestMethod.GET)
//	public void productImage(HttpServletRequest request, HttpServletResponse response, Model model,
//			@RequestParam("id") Long id) throws IOException {
//		Customers item = null;
//		if (id != null) {
//			item = this.customerDAO.findById(id);
//		}
//		if (item != null && item.getImage() != null) {
//			response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
//			response.getOutputStream().write(item.getImage());
//		}
//		response.getOutputStream().close();
//	}

	// GET: Hiển thị product
	@RequestMapping(value = Consts.adminPath + "/customers/edit", method = RequestMethod.GET)
	public String editUser(Model model, @RequestParam(value = "id", defaultValue = "0") Long id) {
		CustomerForm2 form = null;

		if (id != null) {
			Customers item = customerDAO.findById(id);
			if (item != null) {
				form = new CustomerForm2(item);
			}
		}
		if (form == null) {
			form = new CustomerForm2();
			form.setNewMode(true);
		}
		model.addAttribute("form", form);
		return "customer/edit";
	}

	// POST: Save product
	@RequestMapping(value = Consts.adminPath + "/customers/save", method = RequestMethod.POST)
	public String productSave(Model model, //
			@ModelAttribute("form") @Valid CustomerForm2 form, //
			BindingResult result, //
			final RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return "customer/edit";
		}
		try {
			customerDAO.save(form);
		} catch (Exception e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			String message = rootCause.getMessage();
			model.addAttribute("errorMessage", message);
			// Show product form.
			return "customer/edit";
		}

		return "redirect:/admin/customers";
	}

	@RequestMapping(value = Consts.adminPath + "/customers/delete", method = RequestMethod.GET)
	public String deleteUser(@RequestParam("id") Long id, Model model) {
		customerDAO.delete(id);
		return "redirect:/admin/customers";
	}

}
