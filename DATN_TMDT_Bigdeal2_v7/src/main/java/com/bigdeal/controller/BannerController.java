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
import com.bigdeal.dao.BannerDAO;
import com.bigdeal.dao.BlogDAO;
import com.bigdeal.dao.CategoryDAO;
import com.bigdeal.entity.Banner;
import com.bigdeal.entity.Blogs;
import com.bigdeal.form.BannerForm;
import com.bigdeal.form.BlogForm;
import com.bigdeal.model.BlogModel;
import com.bigdeal.pagination.PaginationResult;

@Controller
@Transactional
public class BannerController {

	@Autowired
	private BannerDAO bannerDAO;
	@Autowired
	private CategoryDAO categoryDAO;

	// Danh sách sản phẩm.
	@RequestMapping(value = Consts.adminPath + "/banners", method = RequestMethod.GET)
	public String listProductHandler(Model model, //
			@RequestParam(value = "name", defaultValue = "") String likeName,// thanh search trên menu admin
			@RequestParam(value = "page", defaultValue = "1") int page) {

		model.addAttribute("data", bannerDAO.findAll());
		return "banner/index";
	}

	@RequestMapping(value = { "/banners/image" }, method = RequestMethod.GET)
	public void productImage(HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam("id") Long id) throws IOException {
		Banner item = null;
		if (id != null) {
			item = this.bannerDAO.findById(id);
		}
		if (item != null && item.getImage() != null) {
			response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
			response.getOutputStream().write(item.getImage());
		}
		response.getOutputStream().close();
	}

	@RequestMapping(value = { "/banners/image2" }, method = RequestMethod.GET)
	public void productImage2(HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam("id") Long id) throws IOException {
		Banner item = null;
		if (id != null) {
			item = this.bannerDAO.findById(id);
		}
		if (item != null && item.getImage2() != null) {
			response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
			response.getOutputStream().write(item.getImage());
		}
		response.getOutputStream().close();
	}

	// GET: Hiển thị product
	@RequestMapping(value = Consts.adminPath + "/banners/edit", method = RequestMethod.GET)
	public String editUser(Model model, @RequestParam(value = "id", defaultValue = "0") Long id) {
		BannerForm form = null;

		if (id != null) {
			Banner item = bannerDAO.findById(id);
			if (item != null) {
				form = new BannerForm(item);
			}
		}
		if (form == null) {
			form = new BannerForm();
			form.setNewMode(true);
		}
		model.addAttribute("form", form);
		model.addAttribute("categories", categoryDAO.findAll());
		return "banner/edit";
	}

	// POST: Save product
	@RequestMapping(value = Consts.adminPath + "/banners/save", method = RequestMethod.POST)
	public String productSave(Model model, //
			@ModelAttribute("form") @Valid BannerForm form, //
			BindingResult result, //
			final RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return "banner/edit";
		}
		try {
			bannerDAO.save(form);
		} catch (Exception e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			String message = rootCause.getMessage();
			model.addAttribute("errorMessage", message);
			// Show product form.
			return "banner/edit";
		}

		return "redirect:/admin/banners";
	}

	@RequestMapping(value = Consts.adminPath + "/banners/delete", method = RequestMethod.GET)
	public String deleteUser(@RequestParam("id") Long id, Model model) {
		bannerDAO.delete(id);
		return "redirect:/admin/banners";
	}

}
