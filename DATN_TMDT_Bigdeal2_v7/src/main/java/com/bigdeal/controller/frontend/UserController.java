package com.bigdeal.controller.frontend;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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
import com.bigdeal.model.CartLineInfo;
import com.bigdeal.model.CustomerInfo;
import com.bigdeal.model.GooglePojo;
import com.bigdeal.model.ProductInfo;
import com.bigdeal.model.WishModel;
import com.bigdeal.service.GoogleUtils;
import com.bigdeal.service.RestFB;
import com.bigdeal.utils.Utils;

@Controller
@Transactional
public class UserController {
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

	
	@Autowired
	private JavaMailSender mailSender;
	
	
	@RequestMapping({ "/user/buyProduct" })
	public String listProductHandler(HttpServletRequest request, Model model, //
			@RequestParam(value = "code", defaultValue = "") String code) {

		Product product = null;
		if (code != null && code.length() > 0) {
			product = productDAO.findProduct(code);
		}
		if (product != null) {
			CartInfo cartInfo = Utils.getCartInSession(request);
			ProductInfo productInfo = new ProductInfo(product);
			cartInfo.addProduct(productInfo, 1);				
		}
		model.addAttribute("categories", categoryDAO.findAll());
		model.addAttribute("brands", brandDAO.findAll());
		model.addAttribute("banners", bannerDAO.findAll());
		return "redirect:/user/shoppingCart";
	}

	// GET: Hiển thị trang login
	@RequestMapping(value = { "/usr/login" }, method = RequestMethod.GET)
	public String login(Model model) {
		model.addAttribute("categories", categoryDAO.findAll());
		model.addAttribute("brands", brandDAO.findAll());
		model.addAttribute("banners", bannerDAO.findAll());
		return "/frontend/login";
	}

	
	
	
	// GET: Hiển thị giỏ hàng.
	@RequestMapping(value = { "/user/shoppingCart" }, method = RequestMethod.GET)
	public String shoppingCartHandler(HttpServletRequest request, Model model) {
		model.addAttribute("categories", categoryDAO.findAll());
		model.addAttribute("brands", brandDAO.findAll());
		model.addAttribute("banners", bannerDAO.findAll());
		CartInfo myCart = Utils.getCartInSession(request);
		
//		double total = myCart.getCartLines().stream().map(x -> x.getAmount()).collect(Collectors.summingDouble(Double::doubleValue));
		model.addAttribute("cartForm", myCart);

//		model.addAttribute("total", total);
		return "/frontend/cart";
	}

	@RequestMapping({ "/user/shoppingCartRemoveProduct" })
	public String removeProductHandler(HttpServletRequest request, Model model, //
			@RequestParam(value = "code", defaultValue = "") String code) {
		Product product = null;
		if (code != null && code.length() > 0) {
			product = productDAO.findProduct(code);
		}
		if (product != null) {

			CartInfo cartInfo = Utils.getCartInSession(request);

			ProductInfo productInfo = new ProductInfo(product);

			cartInfo.removeProduct(productInfo);

		}

		return "redirect:/user/shoppingCart";
	}

	// POST: Cập nhập số lượng cho các sản phẩm đã mua.
	@RequestMapping(value = { "/user/shoppingCart" }, method = RequestMethod.POST)
	public String shoppingCartUpdateQty(HttpServletRequest request, //
			Model model, //
			@ModelAttribute("cartForm") CartInfo cartForm) {

		CartInfo cartInfo = Utils.getCartInSession(request);
		cartInfo.updateQuantity(cartForm);
		cartInfo.getQuantityTotal();
model.addAttribute("cart", cartInfo.getQuantityTotal());
		return "redirect:/user/shoppingCart";
	}

	@RequestMapping(value = { "/user/checkout" }, method = RequestMethod.GET)
	public String shoppingCartCustomerForm(HttpServletRequest request, Model model) {

		CartInfo cartInfo = Utils.getCartInSession(request);

		if (cartInfo.isEmpty()) {

			return "redirect:/user/shoppingCart";
		}
		CustomerInfo customerInfo = cartInfo.getCustomerInfo();

		CustomerForm customerForm = new CustomerForm(customerInfo);

		model.addAttribute("customerForm", customerForm);
		model.addAttribute("cartInfo", cartInfo);
		return "/frontend/checkout";
	}

	//checkout
	@RequestMapping(value = { "/user/checkout" }, method = RequestMethod.POST)
		public String shoppingCartCustomerSave(HttpServletRequest request, //
				Model model, //
				@ModelAttribute("customerForm") @Valid CustomerForm customerForm, //
				BindingResult result, //
				final RedirectAttributes redirectAttributes) throws MessagingException, UnsupportedEncodingException {
			CartInfo cartInfo = Utils.getCartInSession(request);
			if (result.hasErrors()) {
				customerForm.setValid(false);
				model.addAttribute("cartInfo", cartInfo);
				// Forward tới trang nhập lại.
				return "/frontend/checkout";
			}

			customerForm.setValid(true);
			
			CustomerInfo customerInfo = new CustomerInfo(customerForm);
			cartInfo.setCustomerInfo(customerInfo);

			for(CartLineInfo  cart: cartInfo.getCartLines()) {
				System.out.println(" code: "+cart.getProductInfo().getCode());
				System.out.println("số lượng:  "+cart.getQuantity());
				
				productDAO.Update(cart.getProductInfo().getCode(), cart.getQuantity());
			}
			
			
			//////////////////////////sendmail///////////////////////////////
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message,true);
			
			String email = request.getParameter("email");
			
			
			System.out.println(email);
			String mailSubject = "TTQDN";
			String mailContent ="<img src ='cid:logoImage' height=\"150px\" width=\"330px\" /><br>";
			 		mailContent +=  "Cảm ơn quý khách đã hàng tại<strong> <a href=\"https://localhost:8443/\" style=\"color: black ;font-style: none\">TTQDN</a></strong> "+"<br>"; 
					for(CartLineInfo  cart: cartInfo.getCartLines()) {
					mailContent +="Sản Phẩm: "+ cart.getProductInfo().getName() + "Số lượng: " + cart.getQuantity() +"\r<br><br>";		
					}
					mailContent +="Thông tin chi tiết xin liên hệ: <br>";
					mailContent +="- <strong><a href=\"https://localhost:8443/frontend/contact\" style=\"color: black ;font-style: none\">Phản hồi khách hàng</a></strong>";
					
					
			helper.setFrom("Duyduyly123vn@gmail.com","TTQDN");
			//tên đại diên Demo
			helper.setTo(email);
			
			helper.setSubject(mailSubject);
			helper.setText(mailContent,true);
			
			ClassPathResource resource = new ClassPathResource("/static/logo/logo3.jpg");
			helper.addInline("logoImage", resource);
			
			mailSender.send(message);
			/////////////////////////////////////////////////////////////////
			
			return "redirect:/user/shoppingCartConfirmation";
		}

	@RequestMapping(value = { "/user/shoppingCartConfirmation" }, method = RequestMethod.GET)
	public String shoppingCartConfirmationReview(HttpServletRequest request, Model model) {
		CartInfo cartInfo = Utils.getCartInSession(request);

		if (cartInfo == null || cartInfo.isEmpty()) {

			return "redirect:/user/shoppingCart";
		} else if (!cartInfo.isValidCustomer()) {

			return "redirect:/user/checkout";
		}
		model.addAttribute("cartInfo", cartInfo);
		try {
			orderDAO.saveOrder(cartInfo);
		} catch (Exception e) {

			return "/frontend/checkout";
		}

		// Xóa giỏ hàng khỏi session.
		Utils.removeCartInSession(request);

		// Lưu thông tin đơn hàng cuối đã xác nhận mua.
		Utils.storeLastOrderedCartInSession(request, cartInfo);
		return "/frontend/order-success";
	}

	@RequestMapping({ "/user/order-history" })
	public String orderHistory(HttpServletRequest request, Model model, Principal principal) {

		model.addAttribute("categories", categoryDAO.findAll());
		model.addAttribute("brands", brandDAO.findAll());
		model.addAttribute("banners", bannerDAO.findAll());
		List<Order> orders = orderDAO.findByUsername(principal.getName());
		model.addAttribute("orders", orders);
		return "/frontend/order-history";
	}

	@RequestMapping({ "/user/addWish" })
	public String addWish(HttpServletRequest request, Model model, //
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
		model.addAttribute("categories", categoryDAO.findAll());
		model.addAttribute("brands", brandDAO.findAll());
		model.addAttribute("banners", bannerDAO.findAll());
		return "redirect:/user/wishList";
	}

	// GET: Hiển thị trang yêu thích sản phẩm.
	@RequestMapping(value = { "/user/wishList" }, method = RequestMethod.GET)
	public String wishList(HttpServletRequest request, Model model, Principal principal) {
		model.addAttribute("categories", categoryDAO.findAll());
		model.addAttribute("brands", brandDAO.findAll());
		model.addAttribute("banners", bannerDAO.findAll());
		List<Wish> listWish = wishDAO.findByUsername(principal.getName());
		List<WishModel> listModel = new ArrayList<WishModel>();
		for (Wish wish : listWish) {
			WishModel wishModel = new WishModel();
			wishModel.setId(wish.getId());
			wishModel.setUserName(principal.getName());
			wishModel.setProductCode(wish.getProductCode());
			Product p = productDAO.findProduct(wish.getProductCode());
			wishModel.setProductName(p.getName());
			wishModel.setPrice(p.getPrice());
			listModel.add(wishModel);
		}
		model.addAttribute("wishs", listModel);
		return "/frontend/wishlist";
	}

	@RequestMapping({ "/user/removeWish" })
	public String removeWish(HttpServletRequest request, Model model, //
			@RequestParam(value = "id", defaultValue = "") Long id) {

		wishDAO.delete(id);
		model.addAttribute("categories", categoryDAO.findAll());
		model.addAttribute("brands", brandDAO.findAll());
		model.addAttribute("banners", bannerDAO.findAll());
		return "redirect:/user/wishList";
	}

	@RequestMapping(value = { "/user/rating" }, method = RequestMethod.POST)
	public String rating(HttpServletRequest request, //
			Model model, //
			@ModelAttribute("productRatingForm") ProductRatingForm form, Principal principal) {
		form.setUserName(principal.getName());
		productRatingDAO.save(form);
		return "redirect:/product-detail?code=" + form.getProductCode();
	}

	// GET: Hiển thị trang login
	@RequestMapping(value = { "/usr/register" }, method = RequestMethod.GET)
	public String register(Model model) {
		model.addAttribute("categories", categoryDAO.findAll());
		model.addAttribute("brands", brandDAO.findAll());
		model.addAttribute("banners", bannerDAO.findAll());
		model.addAttribute("accountForm", new AccountForm());
		return "/frontend/register";
	}

	//thêm check user confim(xác nhận mật khẩu)
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String accountSave(Model model, //
			@ModelAttribute("accountForm") @Valid AccountForm form, //
			BindingResult result, //
			final RedirectAttributes redirectAttributes, HttpServletRequest rq) {

		String confim = rq.getParameter("confim");
		String password = rq.getParameter("password");
		String username = rq.getParameter("userName");
		System.out.println("username: " + username);
		if (result.hasErrors()) {
			model.addAttribute("categories", categoryDAO.findAll());
			model.addAttribute("brands", brandDAO.findAll());
			model.addAttribute("banners", bannerDAO.findAll());

			return "frontend/register";
		}
		try {

			if (checkAccount(username) == false  ) {
				model.addAttribute("checkuser", "Mời bạn nhập Username khác đã có người sử dụng username này");
				System.out.println(checkAccount(username));
				System.out.println("đã trùng khóa chính");
				return "frontend/register";
			} else {
				
				if(confim.equals(password)) {
				form.setUserRole(Consts.ROLE_USER);
				accountDAO.save(form);
				
				}else {
					model.addAttribute("checkuser", "mật khẩu và xác nhận mật khẩu không trùng nhau");
					return "frontend/register";
				}
			}
		} catch (Exception e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			String message = rootCause.getMessage();
			model.addAttribute("errorMessage", message);
			// Show product form.
			return "frontend/register";
		}

		return "redirect:/usr/login";
	}

	@RequestMapping("/login-facebook")
	public String loginFacebook(HttpServletRequest request) throws ClientProtocolException, IOException {
		String code = request.getParameter("code");

		if (code == null || code.isEmpty()) {
			return "redirect:/usr/login?facebook=error";
		}
		String accessToken = restFb.getToken(code);

		com.restfb.types.User user = restFb.getUserInfo(accessToken);
		UserDetails userDetail = restFb.buildUser(user);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail, null,
				userDetail.getAuthorities());
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return "redirect:/";
	}

	@RequestMapping("/login-google")
	public String loginGoogle(HttpServletRequest request) throws ClientProtocolException, IOException {
		String code = request.getParameter("code");

		if (code == null || code.isEmpty()) {
			return "redirect:/usr/login?google=error";
		}
		String accessToken = googleUtils.getToken(code);

		GooglePojo googlePojo = googleUtils.getUserInfo(accessToken);
		UserDetails userDetail = googleUtils.buildUser(googlePojo);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail, null,
				userDetail.getAuthorities());
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return "redirect:/";
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
