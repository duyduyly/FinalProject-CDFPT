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
import com.bigdeal.dao.BlogDAO;
import com.bigdeal.entity.Blogs;
import com.bigdeal.form.BlogForm;
import com.bigdeal.model.BlogModel;
import com.bigdeal.pagination.PaginationResult;

@Controller
@Transactional
public class BlogController {

	@Autowired
	private BlogDAO blogDAO;

	// Danh sách sản phẩm.
	@RequestMapping(value = Consts.adminPath + "/blogs", method = RequestMethod.GET)
	public String listProductHandler(Model model, //
			@RequestParam(value = "name", defaultValue = "") String likeName,
			@RequestParam(value = "page", defaultValue = "1") int page) {

		PaginationResult<BlogModel> result = blogDAO.query(page, //
				Consts.RESULT_PER_PAGE, Consts.MAX_NAVIGATION_PAGE, likeName);

		model.addAttribute("data", result);
		return "blog/index";
	}

	@RequestMapping(value = { Consts.adminPath + "/blogs/image" }, method = RequestMethod.GET)
	public void productImage(HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam("id") Long id) throws IOException {
		Blogs item = null;
		if (id != null) {
			item = this.blogDAO.findById(id);
		}
		if (item != null && item.getImage() != null) {
			response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
			response.getOutputStream().write(item.getImage());
		}
		response.getOutputStream().close();
	}
	@RequestMapping(value = { "/blogs/image" }, method = RequestMethod.GET)
	public void blogImage(HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam("id") Long id) throws IOException {
		Blogs item = null;
		if (id != null) {
			item = this.blogDAO.findById(id);
		}
		if (item != null && item.getImage() != null) {
			response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
			response.getOutputStream().write(item.getImage());
		}
		response.getOutputStream().close();
	}

	// GET: Hiển thị product
	@RequestMapping(value = Consts.adminPath + "/blogs/edit", method = RequestMethod.GET)
	public String editUser(Model model, @RequestParam(value = "id", defaultValue = "0") Long id) {
		BlogForm form = null;

		if (id != null) {
			Blogs item = blogDAO.findById(id);
			if (item != null) {
				form = new BlogForm(item);
			}
		}
		if (form == null) {
			form = new BlogForm();
			form.setNewMode(true);
		}
		model.addAttribute("form", form);
		return "blog/edit";
	}

	// POST: Save product
	@RequestMapping(value = Consts.adminPath + "/blogs/save", method = RequestMethod.POST)
	public String productSave(Model model, //
			@ModelAttribute("form") @Valid BlogForm form, //
			BindingResult result, //
			final RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return "blog/edit";
		}
		try {
			blogDAO.save(form);
		} catch (Exception e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			String message = rootCause.getMessage();
			model.addAttribute("errorMessage", message);
			// Show product form.
			return "blog/edit";
		}

		return "redirect:/admin/blogs";
	}

	@RequestMapping(value = Consts.adminPath + "/blogs/delete", method = RequestMethod.GET)
	public String deleteUser(@RequestParam("id") Long id, Model model) {
		blogDAO.delete(id);
		return "redirect:/admin/blogs";
	}

}
