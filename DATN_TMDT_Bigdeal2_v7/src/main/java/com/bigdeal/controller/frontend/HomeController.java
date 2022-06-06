package com.bigdeal.controller.frontend;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bigdeal.constants.Consts;
import com.bigdeal.dao.AccountDAO;
import com.bigdeal.dao.BannerDAO;
import com.bigdeal.dao.BlogDAO;
import com.bigdeal.dao.BrandDAO;
import com.bigdeal.dao.CategoryDAO;
import com.bigdeal.dao.ProductDAO;
import com.bigdeal.dao.ProductRatingDAO;
import com.bigdeal.entity.Account;
import com.bigdeal.entity.Brands;
import com.bigdeal.entity.Product;
import com.bigdeal.entity.ProductRating;
import com.bigdeal.form.ProductRatingForm;
import com.bigdeal.pagination.PaginationResult;

@Controller
@Transactional
public class HomeController {

	final String MANAGER = "ROLE_MANAGER";
	final String EMPLOYEE = "ROLE_EMPLOYEE";

	@Autowired
	private CategoryDAO categoryDAO;
	@Autowired
	private AccountDAO accountDAO;

	@Autowired
	private BrandDAO brandDAO;
	@Autowired
	private BannerDAO bannerDAO;
	@Autowired
	private ProductDAO productDAO;
	@Autowired
	private BlogDAO blogDAO;

	@Autowired
	private ProductRatingDAO productRatingDAO;

	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("categories", categoryDAO.findAll());
		model.addAttribute("brands", brandDAO.findAll());
		model.addAttribute("banners", bannerDAO.findAll());
		model.addAttribute("blogs", blogDAO.findAll());

		List<Product> listpdList = productDAO.findAll_sp();
		model.addAttribute("product", listpdList);
		
		
		
//		for(Brands brands :brandDAO.findAll()) {
//			System.out.println("tên brand: " + brands.getBrandName());
//			System.out.println("tên id: " + brands.getId());
//			System.out.println("tên category id: " + brands.getCategoriesId().getId());
//			System.out.println("tên category Name: " + brands.getCategoriesId().getCategoryName());
//			
//			System.out.println("-------------------------------------");
//		}
		return "/frontend/home";
	}



	// product detail
		@RequestMapping("/product-detail")
		public String productDetail(Model model, @RequestParam(value = "code", required = true) String code) {
			model.addAttribute("categories", categoryDAO.findAll());
			model.addAttribute("brands", brandDAO.findAll());
			model.addAttribute("banners", bannerDAO.findAll());
			Product product = productDAO.findProduct(code);
			model.addAttribute("product", product);

			List<Product> products = productDAO.findByCategory(product.getCategoryId(), 1, 100, 100).getList();
			model.addAttribute("products", products);

			ProductRatingForm ratingForm = new ProductRatingForm();
			ratingForm.setProductCode(code);
			model.addAttribute("productRatingForm", ratingForm);
			int total = 0;
			int star = 0;

			List<ProductRating> productMessages = productRatingDAO.findByProductCode(code);
			for (ProductRating productRating : productMessages) {
				total += productRating.getRating();
			}
			if (productMessages.size() > 0) {
				star = total / productMessages.size();
			}

			model.addAttribute("star", star);
			model.addAttribute("ratingMessage", productMessages);
			
			
			List<Product> listByBrandId = findByBrandId(code);
			List<Brands> listFindBy_CategoryId = findByCategoryId(code);
			
			model.addAttribute("listByBrandId", listByBrandId);
			model.addAttribute("listFindBy_CategoryId", listFindBy_CategoryId);
			
//			new product
			int i = 0;
			List<Product> list_top3_product_new = new ArrayList<Product>();
			for (Product pd : productDAO.findProduct_Status("new")) {
				list_top3_product_new.add(pd);
				i++;
				if (i == 3) {
					break;
				}

			}
			model.addAttribute("top3_product_news", list_top3_product_new);
			
			return "/frontend/product-detail";
		}

	@RequestMapping("/blog-detail")
	public String blogDetail(Model model, @RequestParam(value = "blogId", required = true) Long blogId) {
		model.addAttribute("categories", categoryDAO.findAll());
		model.addAttribute("brands", brandDAO.findAll());
		model.addAttribute("banners", bannerDAO.findAll());
		model.addAttribute("blog", blogDAO.findById(blogId));

		return "/frontend/blog-details";
	}

	@RequestMapping("/tam")
	public String LoginTam(HttpServletRequest rq) {
		String role = "";

		String username = rq.getRemoteUser();

		System.out.println("Username: " + username);

		List<Account> listaccount = accountDAO.findAll();
		for (Account ac : listaccount) {
			System.out.println(ac.getUserName());
			if (ac.getUserName().equalsIgnoreCase(username) && ac.getUserRole().equalsIgnoreCase(MANAGER)) {
				role = ac.getUserRole();
				System.out.println(role);
				break;
			} else if (ac.getUserName().equalsIgnoreCase(username) && ac.getUserRole().equalsIgnoreCase(EMPLOYEE)) {
				role = ac.getUserRole();
				System.out.println(role);
				break;
			}

			System.out.println(role);

		}
		if (role.equalsIgnoreCase(MANAGER)) {

			System.out.println("admin");
			return "redirect:/admin";
		} else {

			System.out.println("store");
			return "redirect:/";

		}
	}
	
public List<Product> findByBrandId(String code) {
		
		Product product = productDAO.findProduct(code);
		
		Long Brandid = product.getBrandId();
		
		List<Product> listByBrandId = productDAO.findbyBrand(Brandid);
		
		System.out.println("-----------theo brand id--------------");
		for(Product pd : listByBrandId) {
			System.out.println("Mã Code: " +pd.getCode());
		System.out.println("Mã Brand: " +pd.getBrandId());
		
		}
		
		return listByBrandId;
	}
	
public List<Brands> findByCategoryId(String code) {
		
		Product product = productDAO.findProduct(code);
		
		Long categoryId = product.getCategoryId();
		
		List<Brands> listFindBy_CategoryId = brandDAO.findByCategoryId(categoryId);
		
		System.out.println("========THeo category========");
		for(Brands pd : listFindBy_CategoryId) {
		System.out.println("Mã Brand: " +pd.getId());
		System.out.println("tên Brand: " +pd.getBrandName());
		
		}
		
		return listFindBy_CategoryId;
	}

}
