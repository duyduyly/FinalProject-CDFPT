package com.bigdeal.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bigdeal.constants.Consts;
import com.bigdeal.dao.OrderDAO;
import com.bigdeal.dao.ProductDAO;
import com.bigdeal.entity.OrderDetail;
import com.bigdeal.entity.Product;
import com.bigdeal.form.CustomerForm;
import com.bigdeal.model.CartInfo;
import com.bigdeal.model.CustomerInfo;
import com.bigdeal.model.OrderDetailInfo;
import com.bigdeal.model.OrderInfo;
import com.bigdeal.model.ProductInfo;
import com.bigdeal.pagination.PaginationResult;
import com.bigdeal.utils.Utils;
import com.bigdeal.validator.CustomerFormValidator;

@Controller
@Transactional
public class MainController {

	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	private ProductDAO productDAO;

	@Autowired
	private CustomerFormValidator customerFormValidator;

	@Autowired
	MessageSource messageSource;

//	@InitBinder
//	public void myInitBinder(WebDataBinder dataBinder) {
//		Object target = dataBinder.getTarget();
//		if (target == null) {
//			return;
//		}
//		System.out.println("Target=" + target);
//
//		// Tr?????ng h???p update SL tr??n gi??? h??ng.
//		// (@ModelAttribute("cartForm") @Validated CartInfo cartForm)
//		if (target.getClass() == CartInfo.class) {
//
//		}
//
//		// Tr?????ng h???p save th??ng tin kh??ch h??ng.
//		// (@ModelAttribute @Validated CustomerInfo customerForm)
//		else if (target.getClass() == CustomerForm.class) {
//			dataBinder.setValidator(customerFormValidator);
//		}
//
//	}

	@RequestMapping("/403")
	public String accessDenied() {
		return "/error/403";
	}

	@RequestMapping("/usr/403")
	public String accessDeniedUser() {
		return "/frontend/403";
	}

	@RequestMapping("/admin")
	public String home(@RequestParam(value = "lang", defaultValue = "") String lang, CookieLocaleResolver resolver,
			Model model) {
//		localeResolver.setLocale(request, response, new Locale(lang));
		System.out.println("lang=" + lang);
		resolver.setDefaultLocale(new Locale(lang));
		//T???ng s??? l?????ng s???n ph???m
		List<Product> listpd = productDAO.findAll_sp();
		int count = 0;
		for (Product pd : listpd) {
			count += pd.getAmount();
		}
		System.out.println("s??? l?????ng s???n ph???m: " + count);
		model.addAttribute("CuontProducts", count);
		//T???ng s??? l?????ng ????n h??ng
		int page = 1;
		PaginationResult<OrderInfo> paginationResult //
				= orderDAO.listOrderInfo(page, Consts.RESULT_PER_PAGE, Consts.MAX_NAVIGATION_PAGE);

		model.addAttribute("paginationResult", paginationResult);
		//T???ng s??? l?????ng b??n
				List<OrderDetailInfo> listodt = orderDAO.getAllOrderDetail();
				int countodr = 0;
				for (OrderDetailInfo pdodr : listodt) {
					countodr += pdodr.getQuanity();
				}
				System.out.println("s??? l?????ng s???n ph???m: " + (count - countodr));
				model.addAttribute("Cuontoder", countodr);
				model.addAttribute("Cuonton", count - countodr);

		
		return "/dashboard/index";
	}
//	@RequestMapping("/changeLang")
//	public String changeLang(@RequestParam(value = "lang", defaultValue = "") String lang, SessionLocaleResolver localeResolver) {
//		
//		return "/admin";
//	}
	// GET: Nh???p th??ng tin kh??ch h??ng.

	// POST: Save th??ng tin kh??ch h??ng.

	// GET: Xem l???i th??ng tin ????? x??c nh???n.

	// POST: G???i ????n h??ng (Save).
	@RequestMapping(value = { "/shoppingCartConfirmation" }, method = RequestMethod.POST)

	public String shoppingCartConfirmationSave(HttpServletRequest request, Model model) {
		CartInfo cartInfo = Utils.getCartInSession(request);

		if (cartInfo.isEmpty()) {

			return "redirect:/shoppingCart";
		} else if (!cartInfo.isValidCustomer()) {

			return "redirect:/shoppingCartCustomer";
		}
		try {
			orderDAO.saveOrder(cartInfo);
		} catch (Exception e) {

			return "shoppingCartConfirmation";
		}

		// X??a gi??? h??ng kh???i session.
		Utils.removeCartInSession(request);

		// L??u th??ng tin ????n h??ng cu???i ???? x??c nh???n mua.
		Utils.storeLastOrderedCartInSession(request, cartInfo);

		return "redirect:/shoppingCartFinalize";
	}

	@RequestMapping(value = { "/shoppingCartFinalize" }, method = RequestMethod.GET)
	public String shoppingCartFinalize(HttpServletRequest request, Model model) {

		CartInfo lastOrderedCart = Utils.getLastOrderedCartInSession(request);

		if (lastOrderedCart == null) {
			return "redirect:/shoppingCart";
		}
		model.addAttribute("lastOrderedCart", lastOrderedCart);
		return "shoppingCartFinalize";
	}

}
