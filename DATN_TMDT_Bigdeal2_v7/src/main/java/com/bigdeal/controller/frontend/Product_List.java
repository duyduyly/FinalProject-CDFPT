package com.bigdeal.controller.frontend;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bigdeal.constants.Consts;
import com.bigdeal.dao.AccountDAO;
import com.bigdeal.dao.BannerDAO;
import com.bigdeal.dao.BlogDAO;
import com.bigdeal.dao.BrandDAO;
import com.bigdeal.dao.CategoryDAO;
import com.bigdeal.dao.ProductDAO;
import com.bigdeal.dao.ProductRatingDAO;
import com.bigdeal.dao.WishDAO;
import com.bigdeal.entity.Brands;
import com.bigdeal.entity.Product;
import com.bigdeal.pagination.PaginationResult;

@Controller
@Transactional
public class Product_List {

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
	private WishDAO wishDAO;

	@Autowired
	private ProductRatingDAO productRatingDAO;

	@GetMapping("/product-list")
	public String productList_demo(Model model, @RequestParam(value = "categoryId", required = false) Long categoryId,
			@RequestParam(value = "brandId", required = false) Long brandId,
			@RequestParam(value = "price", required = false) Double price,
			@RequestParam(value = "productName", required = false) String productName,
			@RequestParam(value = "page", defaultValue = "1") int page, HttpSession session) {
		model.addAttribute("categories", categoryDAO.findAll());
		model.addAttribute("brands", brandDAO.findAll());
		model.addAttribute("banners", bannerDAO.findAll());

		PaginationResult<Product> products;

		System.out.println("category: " + categoryId);
		System.out.println("brand: " + brandId);
		System.out.println("price: " + price);

		session.setAttribute("brandId", brandId);
		session.setAttribute("categoryId", categoryId);
		session.setAttribute("price", price);

		if (brandId != null) {
			session.removeAttribute("categoryId");
			session.removeAttribute("price");
		} else if (price != null) {
			session.removeAttribute("categoryId");
			session.removeAttribute("brandId");
		} else if (categoryId != null) {
			session.removeAttribute("price");
			session.removeAttribute("brandId");
		}

		products = list_PaginationResult(categoryId, brandId, price, page, productName);

		model.addAttribute("products", products);

		model.addAttribute("productsnew", productDAO.findProduct_Status("new"));

		int i = 0;
		List<Product> list_top5_product_new = new ArrayList<Product>();
		for (Product pd : productDAO.findProduct_Status("new")) {
			list_top5_product_new.add(pd);
			i++;
			if (i == 3) {
				break;
			}

		}

		model.addAttribute("product", productDAO.findAll_sp());

		model.addAttribute("category", categoryDAO.findAll());

		model.addAttribute("brand_filter", findByCategory(brandId, categoryId, price));

		model.addAttribute("top5_product_news", list_top5_product_new);
		System.out.println("yyyyyyyyyyyyyy");

		return "/frontend/product-list";

	}

	@GetMapping("/product-list-page-demo")
	public String productList_page_demo(Model model,
			@RequestParam(value = "categoryId", required = false) Long categoryId,
			@RequestParam(value = "brandId", required = false) Long brandId,
			@RequestParam(value = "price", required = false) Double price,
			@RequestParam(value = "productName", required = false) String productName,
			@RequestParam(value = "page", defaultValue = "1") int page, HttpSession session) {
		model.addAttribute("categories", categoryDAO.findAll());
		model.addAttribute("brands", brandDAO.findAll());
		model.addAttribute("banners", bannerDAO.findAll());

		PaginationResult<Product> products;
		categoryId = (Long) session.getAttribute("categoryId");
		brandId = (Long) session.getAttribute("brandId");
		price = (Double) session.getAttribute("price");

		products = list_PaginationResult(categoryId, brandId, price, page, productName);

		model.addAttribute("products", products);

		model.addAttribute("productsnew", productDAO.findProduct_Status("new"));

		int i = 0;
		List<Product> list_top5_product_new = new ArrayList<Product>();
		for (Product pd : productDAO.findProduct_Status("new")) {
			list_top5_product_new.add(pd);
			i++;
			if (i == 3) {
				break;
			}

		}
		model.addAttribute("brand_filter", findByCategory(brandId, categoryId, price));
		model.addAttribute("product", productDAO.findAll_sp());
		model.addAttribute("category", categoryDAO.findAll());
		model.addAttribute("brand", brandDAO.findAll());

		model.addAttribute("top5_product_news", list_top5_product_new);
		System.out.println("yyyyyyyyyyyyyy");

		return "/frontend/product-list";

	}

	public PaginationResult<Product> list_PaginationResult(Long categoryId, Long brandId, Double price, int page, String productName) {
		PaginationResult<Product> products;

		if (categoryId != null && price == null && brandId == null) {
			products = productDAO.findByCategory(categoryId, page, //
					Consts.RESULT_PER_PAGE, Consts.MAX_NAVIGATION_PAGE);
		}
		else if (productName != null && !productName.isEmpty()) {
			products = productDAO.findByName(productName, page, //
					Consts.RESULT_PER_PAGE, Consts.MAX_NAVIGATION_PAGE);
		}
		else if (brandId != null && categoryId == null) {
			products = productDAO.findByBrands(brandId, page, //
					Consts.RESULT_PER_PAGE, Consts.MAX_NAVIGATION_PAGE);
		} else if (brandId == null && categoryId == null && price != null) {
			products = productDAO.findByPrice(price, page, //
					Consts.RESULT_PER_PAGE, Consts.MAX_NAVIGATION_PAGE);
		} else {
			products = productDAO.findAll(page, //
					Consts.RESULT_PER_PAGE, Consts.MAX_NAVIGATION_PAGE);
		}

		return products;
	}

	public List<Brands> findByCategory(Long brandId, Long categoryId, Double price) {

		List<Brands> listFindBy = null;
		if (categoryId != null) {
			listFindBy = brandDAO.findByCategoryId(categoryId);
		}
		else if (brandId != null) {
			Brands br = brandDAO.findById(brandId);
			categoryId = br.getCategoriesId().getId();
			listFindBy = brandDAO.findByCategoryId(categoryId);
		}
		else if (price != null) {
			listFindBy = brandDAO.findAll();
		}else {
			listFindBy = brandDAO.findAll();
		}

		for (Brands br : listFindBy) {
			System.out.println("BRandid: " + br.getId());
			System.out.println("Brandid: " + br.getBrandName());
		}

		return listFindBy;
	}

}
