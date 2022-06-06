package com.bigdeal.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bigdeal.constants.Consts;
import com.bigdeal.dao.AccountDAO;
import com.bigdeal.dao.CustomerDAO;
import com.bigdeal.entity.Account;
import com.bigdeal.entity.Customers;
import com.bigdeal.form.AccountForm;
import com.bigdeal.form.CustomerForm2;
import com.bigdeal.model.AccountModel;
import com.bigdeal.model.CustomerModel;
import com.bigdeal.pagination.PaginationResult;

@Controller
@Transactional
public class AccountController {

	@Autowired
	private AccountDAO accountDAO;

	// Danh sách sản phẩm.
	@RequestMapping(value = Consts.adminPath + "/accounts", method = RequestMethod.GET)
	public String listProductHandler(Model model, //
			@RequestParam(value = "name", defaultValue = "") String likeName,
			@RequestParam(value = "page", defaultValue = "1") int page) {

		PaginationResult<AccountModel> result = accountDAO.query(page, //
				Consts.RESULT_PER_PAGE, Consts.MAX_NAVIGATION_PAGE, likeName);

		model.addAttribute("data", result);
		return "account/index";
	}

	
	@RequestMapping(value = { Consts.adminPath + "/accounts/image" }, method = RequestMethod.GET)
	public void productImage(HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam("userName") String userName) throws IOException {
		Account item = null;
		if (userName != null) {
			item = this.accountDAO.findById(userName);
		}
		if (item != null && item.getAvatar() != null) {
			response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
			response.getOutputStream().write(item.getAvatar());
		}
		response.getOutputStream().close();
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

	// GET: Hiển thị account edit
	@RequestMapping(value = Consts.adminPath + "/accounts/edit", method = RequestMethod.GET)
	public String editUser(Model model, @RequestParam(value = "id", defaultValue = "0") String userName) {
		AccountForm form = null;

		if (userName != null) {
			Account item = accountDAO.findById(userName);
			if (item != null) {
				form = new AccountForm(item);
			}
		}
		if (form == null) {
			form = new AccountForm();
			form.setNewMode(true);
		}
		model.addAttribute("form", form);
		return "account/edit";
	}

	// GET: Hiển thị acount edit_update
	@RequestMapping(value = Consts.adminPath + "/accounts/edit_update", method = RequestMethod.GET)
	public String editUser_update(Model model, @RequestParam(value = "id", defaultValue = "0") String userName) {
		AccountForm form = null;

		if (userName != null) {
			Account item = accountDAO.findById(userName);
			if (item != null) {
				form = new AccountForm(item);
			}
		}
		if (form == null) {
			form = new AccountForm();
			form.setNewMode(true);
		}
		model.addAttribute("form", form);
		return "account/edit_update";
	}

	// POST: Save acount
	@RequestMapping(value = Consts.adminPath + "/accounts/save", method = RequestMethod.POST)
	public String accountSave(Model model, //
			@ModelAttribute("form") @Validated AccountForm form, //
			BindingResult result, //
			final RedirectAttributes redirectAttributes, Errors errors, HttpServletRequest rq) {

		String username = rq.getParameter("userName");
		System.out.println(username);
		if (result.hasErrors()) {

			return "account/edit";
		}
		try {
			if (checkAccount(username) == false) {
				model.addAttribute("checkuser", "Mời bạn nhập Username khác đã có người sử dụng username này");
//				System.out.println(checkAccount(username));
//				System.out.println("đã trùng khóa chính");
				return "account/edit";
			} else {
				accountDAO.save(form);
//				System.out.println("save thành công");
				return "redirect:/admin/accounts";
			}
		} catch (Exception e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			String message = rootCause.getMessage();
			model.addAttribute("errorMessage", message);
			// Show product form.
			return "account/edit";
		}

	}

	// POST: update acount
	@RequestMapping(value = Consts.adminPath + "/accounts/update", method = RequestMethod.POST)
	public String accountUpdate(Model model, //
			@ModelAttribute("form") @Validated AccountForm form, //
			BindingResult result, //
			final RedirectAttributes redirectAttributes, Errors errors) {

		if (result.hasErrors()) {

			return "account/edit";
		}
		try {
			System.out.println("update thành công");
			accountDAO.save(form);
			return "redirect:/admin/accounts";

		} catch (Exception e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			String message = rootCause.getMessage();
			model.addAttribute("errorMessage", message);
			// Show product form.
			return "account/edit_update";
		}

	}

	@RequestMapping(value = Consts.adminPath + "/accounts/delete", method = RequestMethod.GET)
	public String deleteUser(@RequestParam("id") String id, Model model) {
		accountDAO.delete(id);
		return "redirect:/admin/accounts";
	}
	
//	 hàm đọc ảnh avatar nơi nào cần đặt image thì th:src="@{|/avatar|}" thẻ sẽ tự đọc src theo username đăng nhập
	@RequestMapping(value = { "/avatar" }, method = RequestMethod.GET)
	public void avater(HttpServletRequest request, HttpServletResponse response, Model model,
			 Principal principal) throws IOException {
		Account account = null;
		if (principal != null) {
			account = this.accountDAO.findById(principal.getName());
		}
		System.out.println(principal);
		if (account != null && account.getAvatar() != null) {
			response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
			response.getOutputStream().write(account.getAvatar());
		}
		response.getOutputStream().close();
	}

	public boolean checkAccount(String username) {
		boolean flag = true;
		// gọi lại listall account
		List<Account> listac = accountDAO.findAll();
		for (Account ac : listac) {
			// so sánh 2 account nếu đúng thì trả về false
			if (ac.getUserName().equalsIgnoreCase(username)) {
				flag = false;
				break;
			} else {
				flag = true;
			}
		}

		if (flag == false) {
			return false;
		} else {
			return true;
		}
	}

}
