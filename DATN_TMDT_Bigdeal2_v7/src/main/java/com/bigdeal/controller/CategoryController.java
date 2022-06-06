package com.bigdeal.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import com.bigdeal.dao.CategoryDAO;
import com.bigdeal.dao.ProductDAO;
import com.bigdeal.entity.Categories;
import com.bigdeal.entity.Product;
import com.bigdeal.form.CategoryForm;
import com.bigdeal.model.CategoryInfo;
import com.bigdeal.pagination.PaginationResult;

@Controller
@Transactional
public class CategoryController {

	public static final String categoryPath = "/admin/categories";

	@Autowired
	private CategoryDAO categoryDAO;

	@Autowired
	private ProductDAO ProductDAO;

	// Danh sách sản phẩm.
	@RequestMapping(value = "/admin/categories", method = RequestMethod.GET)
	public String listProductHandler(Model model, //
			@RequestParam(value = "name", defaultValue = "") String likeName,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		PaginationResult<CategoryInfo> result = categoryDAO.query(page, //
				Consts.RESULT_PER_PAGE, Consts.MAX_NAVIGATION_PAGE, likeName);

		model.addAttribute("data", result);
		return "category/index";
	}


	// GET: Hiển thị product
	@RequestMapping(value = "/admin/category/edit", method = RequestMethod.GET)
	public String editUser(Model model, @RequestParam(value = "id", defaultValue = "0") Long id) {
		CategoryForm form = null;

		if (id != null) {
			Categories category = categoryDAO.findById(id);
			if (category != null) {
				form = new CategoryForm(category);
			}
		}
		if (form == null) {
			form = new CategoryForm();
			form.setNewMode(true);
		}
		model.addAttribute("form", form);
		return "/category/edit";
	}

	
	@RequestMapping(value = { "/category/image" }, method = RequestMethod.GET)
	public void productImage(HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam("id") Long id) throws IOException {
		Categories product = null;
		if (id != null) {
			product = this.categoryDAO.findById(id);
		}
		if (product != null && product.getImage() != null) {
			response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
			response.getOutputStream().write(product.getImage());
		}
		response.getOutputStream().close();
	}

	
	// POST: Save product
	@RequestMapping(value = { "/admin/category" }, method = RequestMethod.POST)
	public String productSave(Model model, //
			@ModelAttribute("form") @Valid CategoryForm form, //
			BindingResult result, //
			final RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return "category/edit";
		}
		try {
			categoryDAO.save(form);
		} catch (Exception e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			String message = rootCause.getMessage();
			model.addAttribute("errorMessage", message);
			// Show product form.
			return "category/edit";
		}

		return "redirect:/admin/categories";
	}

	@RequestMapping(value = "/admin/category/delete", method = RequestMethod.GET)
	public String deleteUser(@RequestParam("id") Long id, Model model) {
		if (checkCategory(id) == false) {

			return "error/403";

		} else {
			categoryDAO.delete(id);
			return "redirect:/admin/categories";
		}
	}

	public boolean checkCategory(long id) {
		boolean flag = true;

		for (Product pd : ProductDAO.findAll_sp()) {
			if (pd.getCategoryId() == id) {
				flag = false;
				break;
			} else {
				flag = true;

			}

		}

		return flag;
	}

}
