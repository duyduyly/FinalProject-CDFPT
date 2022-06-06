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
import com.bigdeal.dao.ContactDAO;
import com.bigdeal.entity.Blogs;
import com.bigdeal.entity.Contact;
import com.bigdeal.form.BlogForm;
import com.bigdeal.form.ContactForm;
import com.bigdeal.model.BlogModel;
import com.bigdeal.model.ContactModel;
import com.bigdeal.pagination.PaginationResult;

@Controller
@Transactional
public class ContactController {

	@Autowired
	private ContactDAO contactDAO;

	// Danh sách sản phẩm.
	@RequestMapping(value = Consts.adminPath + "/contacts", method = RequestMethod.GET)
	public String list(Model model, //
			@RequestParam(value = "name", defaultValue = "") String likeName,
			@RequestParam(value = "page", defaultValue = "1") int page) {

		PaginationResult<ContactModel> result = contactDAO.query(page, //
				Consts.RESULT_PER_PAGE, Consts.MAX_NAVIGATION_PAGE, likeName);

		model.addAttribute("data", result);
		return "contact/index";
	}

	// GET: Hiển thị contact  
	@RequestMapping(value = Consts.adminPath + "/contacts/edit", method = RequestMethod.GET)
	public String edit(Model model, @RequestParam(value = "id", defaultValue = "0") Long id) {
		ContactForm form = null;

		if (id != null) {
			Contact item = contactDAO.findById(id);
			if (item != null) {
				form = new ContactForm(item);
			}
		}
		if (form == null) {
			form = new ContactForm();
			form.setNewMode(true);
		}
		model.addAttribute("form", form);
		return "contact/edit";
	}

	// POST: Save Contact
		@RequestMapping(value ="/contacts/save", method = RequestMethod.POST)
		public String Save(Model model, //
				@ModelAttribute("form") @Validated ContactForm form, //
				BindingResult result, //
				final RedirectAttributes redirectAttributes) {

			if (result.hasErrors()) {
				return "frontend/contact";
			}
			try {
				contactDAO.save(form);
			} catch (Exception e) {
				Throwable rootCause = ExceptionUtils.getRootCause(e);
				String message = rootCause.getMessage();
				model.addAttribute("errorMessage", message);
				// Show product form.
				return "frontend/contact";
			}

			return "redirect:/frontend/contact";
		}

	@RequestMapping(value = Consts.adminPath + "/contacts/delete", method = RequestMethod.GET)
	public String delete(@RequestParam("id") Long id, Model model) {
		contactDAO.delete(id);
		return "redirect:/admin/contacts";
	}

	@RequestMapping(value = "/frontend/contact", method = RequestMethod.GET)
	public String showdemo(Model model, @RequestParam(value = "id", defaultValue = "0") Long id) {
		ContactForm form = null;

		if (id != null) {
			Contact item = contactDAO.findById(id);
			if (item != null) {
				form = new ContactForm(item);
			}
		}
		if (form == null) {
			form = new ContactForm();
			form.setNewMode(true);
		}
		model.addAttribute("form", form);
		return "frontend/contact";
	}

}
