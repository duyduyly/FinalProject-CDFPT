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
import com.bigdeal.dao.BrandDAO;
import com.bigdeal.dao.CategoryDAO;
import com.bigdeal.dao.ProductDAO;
import com.bigdeal.entity.Brands;
import com.bigdeal.entity.Categories;
import com.bigdeal.entity.Product;
import com.bigdeal.form.BrandForm;
import com.bigdeal.model.BrandInfo;
import com.bigdeal.pagination.PaginationResult;

@Controller
@Transactional
public class BrandController {

	@Autowired
	private BrandDAO brandDAO;
	
	@Autowired
	private ProductDAO productDAO;
	@Autowired
	private CategoryDAO categoryDAO;
	
	
	// Danh sách sản phẩm.
	@RequestMapping(value = Consts.adminPath + "/brands", method = RequestMethod.GET)
	public String listProductHandler(Model model, //
			@RequestParam(value = "name", defaultValue = "") String likeName,
			@RequestParam(value = "page", defaultValue = "1") int page) {


		PaginationResult<BrandInfo> result = brandDAO.query(page, //
				Consts.RESULT_PER_PAGE, Consts.MAX_NAVIGATION_PAGE, likeName);

		model.addAttribute("data", result);
		return "brand/index";
	}

	@RequestMapping(value = {"/brands/image" }, method = RequestMethod.GET)
	public void producthome(HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam("id") Long id) throws IOException {
		Brands item = null;
		if (id != null) {
			item = this.brandDAO.findById(id);
		}
		if (item != null && item.getImage() != null) {
			response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
			response.getOutputStream().write(item.getImage());
		}
		response.getOutputStream().close();
	}
	
	@RequestMapping(value = { Consts.adminPath + "/brands/image" }, method = RequestMethod.GET)
	public void productImage(HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam("id") Long id) throws IOException {
		Brands item = null;
		if (id != null) {
			item = this.brandDAO.findById(id);
		}
		if (item != null && item.getImage() != null) {
			response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
			response.getOutputStream().write(item.getImage());
		}
		response.getOutputStream().close();
	}

	// GET: Hiển thị product
	@RequestMapping(value = Consts.adminPath + "/brands/edit", method = RequestMethod.GET)
	public String editUser(Model model, @RequestParam(value = "id", defaultValue = "0") Long id) {
		BrandForm form = null;

		if (id != null) {
			Brands item = brandDAO.findById(id);
			if (item != null) {
				form = new BrandForm(item);
			}
		}
		if (form == null) {
			form = new BrandForm();
			form.setNewMode(true);
		}
		model.addAttribute("form", form);
		model.addAttribute("category", categoryDAO.findAll());
		return "brand/edit";
	}

	// POST: Save product
	@RequestMapping(value = Consts.adminPath + "/brands/save", method = RequestMethod.POST)
	public String productSave(Model model, //
			@ModelAttribute("form") @Valid BrandForm form, //
			BindingResult result, //
			final RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return "brand/edit";
		}
		try {
			brandDAO.save(form);
		} catch (Exception e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			String message = rootCause.getMessage();
			model.addAttribute("errorMessage", message);
			
			// Show product form.
			return "brand/edit";
		}

		return "redirect:/admin/brands";
	}

	@RequestMapping(value = Consts.adminPath + "/brands/delete", method = RequestMethod.GET)
	public String deleteUser(@RequestParam("id") Long id, Model model) {
		if (checkBrand(id) == false) {
			return "error/403";
		} else {
			brandDAO.delete(id);
			return "redirect:/admin/brands";
		}
	}
	
	public boolean checkBrand(long id) {
		boolean flag = true;

		for (Product pd : productDAO.findAll_sp()) {
			if (pd.getBrandId() == id) {
				flag = false;
				break;
			} else {
				flag = true;
				
			}

		}

		return flag;
	}

}
