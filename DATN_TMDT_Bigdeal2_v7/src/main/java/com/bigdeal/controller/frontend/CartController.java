package com.bigdeal.controller.frontend;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bigdeal.constants.Consts;
import com.bigdeal.dao.AccountDAO;
import com.bigdeal.dao.BannerDAO;
import com.bigdeal.dao.BrandDAO;
import com.bigdeal.dao.CategoryDAO;
import com.bigdeal.dao.OrderDAO;
import com.bigdeal.dao.ProductDAO;
import com.bigdeal.dao.ProductRatingDAO;
import com.bigdeal.dao.WishDAO;
import com.bigdeal.entity.Account;
import com.bigdeal.entity.Order;
import com.bigdeal.entity.Product;
import com.bigdeal.entity.Wish;
import com.bigdeal.form.AccountForm;
import com.bigdeal.form.CustomerForm;
import com.bigdeal.form.ProductRatingForm;
import com.bigdeal.form.WishForm;
import com.bigdeal.model.CartInfo;
import com.bigdeal.model.CustomerInfo;
import com.bigdeal.model.GooglePojo;
import com.bigdeal.model.ProductInfo;
import com.bigdeal.model.WishModel;
import com.bigdeal.service.GoogleUtils;
import com.bigdeal.service.RestFB;
import com.bigdeal.utils.Utils;

@RestController
@Transactional
public class CartController {
	@Autowired
	private CategoryDAO categoryDAO;
	@Autowired
	private BrandDAO brandDAO;
	@Autowired
	private BannerDAO bannerDAO;
	@Autowired
	ProductDAO productDAO;
	@Autowired
	private OrderDAO orderDAO;
	@Autowired
	private WishDAO wishDAO;

	@Autowired
	private ProductRatingDAO productRatingDAO;
	@Autowired
	private AccountDAO accountDAO;

	@Autowired
	private RestFB restFb;
	@Autowired
	private GoogleUtils googleUtils;

	@GetMapping("/getWish")
	public ResponseEntity addTodo(Principal principal) {
		if (principal == null) {
			return ResponseEntity.ok().body(0);
		}
		List<Wish> listWish = wishDAO.findByUsername(principal.getName());
		if (listWish == null) {
			listWish = new ArrayList<Wish>();
		}
		// Trả về response với STATUS CODE = 200 (OK)
		// Body sẽ chứa thông tin về đối tượng todo vừa được tạo.
		return ResponseEntity.ok().body(listWish.size());
	}

	@RequestMapping({ "/user/api/addWish" })
	public ResponseEntity addWish(HttpServletRequest request, Model model, //
			@RequestParam(value = "code", defaultValue = "") String code) {

		Product product = null;
		if (code != null && code.length() > 0) {
			product = productDAO.findProduct(code);
		}
		if (product != null) {
			WishForm form = new WishForm();
			form.setProductCode(code);
			//
			wishDAO.save(form);
		}
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		List<Wish> listWish = wishDAO.findByUsername(currentPrincipalName);
		return ResponseEntity.ok().body(listWish.size());
	}

	@RequestMapping({ "/user/addProductToCart" })
	public ResponseEntity addProductToCart(HttpServletRequest request, Model model, //
			@RequestParam(value = "code", defaultValue = "") String code) {
		CartInfo cartInfo = Utils.getCartInSession(request);
		Product product = null;
		if (code != null && code.length() > 0) {
			product = productDAO.findProduct(code);
		}
		if (product != null) {

			ProductInfo productInfo = new ProductInfo(product);
			cartInfo.addProduct(productInfo, 1);
		}

		return ResponseEntity.ok().body(cartInfo.getCartLines().size());
	}

	// GET: Hiển thị giỏ hàng.
	@RequestMapping(value = { "/getCart" }, method = RequestMethod.GET)
	public ResponseEntity getCart(HttpServletRequest request, Model model) {
		CartInfo myCart = Utils.getCartInSession(request);

//			double total = myCart.getCartLines().stream().map(x -> x.getAmount()).collect(Collectors.summingDouble(Double::doubleValue));
		model.addAttribute("cartForm", myCart);

		return ResponseEntity.ok().body(myCart.getCartLines().size());
	}

	// GET: Hiển thị giỏ hàng.
	@RequestMapping(value = { "/getCartList" }, method = RequestMethod.GET)
	public ResponseEntity getCartList(HttpServletRequest request, Model model) {
		CartInfo myCart = Utils.getCartInSession(request);

//			double total = myCart.getCartLines().stream().map(x -> x.getAmount()).collect(Collectors.summingDouble(Double::doubleValue));
		model.addAttribute("cartForm", myCart);

		return ResponseEntity.ok().body(myCart.getCartLines());
	}
}
