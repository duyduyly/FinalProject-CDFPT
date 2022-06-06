package com.bigdeal.controller;

import java.io.IOException;
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
import com.bigdeal.dao.ProductRatingDAO;
import com.bigdeal.entity.Brands;
import com.bigdeal.entity.Categories;
import com.bigdeal.entity.Product;
import com.bigdeal.entity.ProductRating;
import com.bigdeal.form.ProductForm;
import com.bigdeal.model.BrandInfo;
import com.bigdeal.model.CategoryInfo;
import com.bigdeal.model.ProductInfo;
import com.bigdeal.pagination.PaginationResult;

@Controller
@Transactional
public class ProductController {
	@Autowired
	private ProductDAO productDAO;
	@Autowired
	private CategoryDAO categoryDAO;
	@Autowired
	private BrandDAO brandDAO;
	@Autowired
	private ProductRatingDAO productRatingDAO;

	// Danh sách sản phẩm.
	@RequestMapping({ "/admin/products" })
	public String listProductHandler(Model model, //
			@RequestParam(value = "name", defaultValue = "") String likeName, // lấy dữ liệu trên thanh search admin để
																				// tìm kiếm dữ liệu
			@RequestParam(value = "page", defaultValue = "1") int page) {

		PaginationResult<ProductInfo> result = productDAO.queryProducts(page, //
				Consts.RESULT_PER_PAGE, Consts.MAX_NAVIGATION_PAGE, likeName);
		for (ProductInfo productInfo : result.getList()) {
			Categories category = categoryDAO.findById(productInfo.getCategoryId());
			Brands brand = brandDAO.findById(productInfo.getBrandId());
			if (category != null) {
				productInfo.setCategoryName(category.getCategoryName());
			}
			if (brand != null) {
				productInfo.setBrandName(brand.getBrandName());
			}
		}
		model.addAttribute("paginationProducts", result);

		List<Product> listpd = productDAO.findAll_sp();
		int count = 0;
		for (Product pd : listpd) {
			count += pd.getAmount();
		}
		System.out.println("số lượng sản phẩm: " + count);
		model.addAttribute("CuontProducts", count);
		return "product/index";
	}

	// GET: Hiển thị product
	@RequestMapping(value = "/admin/product/edit", method = RequestMethod.GET)
	public String editUser(Model model, @RequestParam(value = "code", defaultValue = "") String code) {
		ProductForm productForm = null;

		if (code != null && code.length() > 0) {
			Product product = productDAO.findProduct(code);
			if (product != null) {
				productForm = new ProductForm(product);
			}
		}
		if (productForm == null) {
			productForm = new ProductForm();
			productForm.setNewProduct(true);
		}
		PaginationResult<CategoryInfo> categories = categoryDAO.query(1, //
				1000, 1000, "");
		PaginationResult<BrandInfo> brands = brandDAO.query(1, //
				1000, 1000, "");
		model.addAttribute("productForm", productForm);
		model.addAttribute("categories", categories);
		model.addAttribute("brands", brands);

		return "/product/edit";
	}

	@RequestMapping(value = "/admin/product/view", method = RequestMethod.GET)
	public String view(Model model, @RequestParam(value = "code", defaultValue = "") String code) {
		if (code != null && code.length() > 0) {
			List<ProductRating> productRatings = productRatingDAO.findByProductCode(code);
			model.addAttribute("productRatings", productRatings);
		} else {
			return "/product/index";
		}

		return "/product/view";
	}

	@RequestMapping(value = "/admin/product/delete", method = RequestMethod.GET)
	public String deleteUser(@RequestParam("code") String code, Model model) {
		productDAO.delete(code);
		return "redirect:/admin/products";
	}

	// POST: Save product
	@RequestMapping(value = { "/admin/product" }, method = RequestMethod.POST)
	public String productSave(Model model, //
			@ModelAttribute("productForm") @Valid ProductForm productForm, //
			BindingResult result, //
			final RedirectAttributes redirectAttributes) {
		PaginationResult<CategoryInfo> categories = categoryDAO.query(1, //
				1000, 1000, "");
		PaginationResult<BrandInfo> brands = brandDAO.query(1, //
				1000, 1000, "");
		model.addAttribute("productForm", productForm);
		model.addAttribute("categories", categories);
		model.addAttribute("brands", brands);
		if (result.hasErrors()) {
			return "product/edit";
		}
		try {
			productDAO.save(productForm);
		} catch (Exception e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			String message = rootCause.getMessage();
			model.addAttribute("errorMessage", message);
			// Show product form.
			return "product/edit";
		}

		return "redirect:/admin/products";
	}

//	 hình ảnh 1
	@RequestMapping(value = { "/productImage" }, method = RequestMethod.GET)
	public void productImage(HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam("code") String code) throws IOException {
		Product product = null;
		if (code != null) {
			product = this.productDAO.findProduct(code);
		}
		if (product != null && product.getImage() != null) {
			response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
			response.getOutputStream().write(product.getImage());
		}
		response.getOutputStream().close();
	}

//	 hình ảnh 2
	@RequestMapping(value = { "/productImage2" }, method = RequestMethod.GET)
	public void productImage2(HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam("code") String code) throws IOException {
		Product product = null;
		if (code != null) {
			product = this.productDAO.findProduct(code);
		}
		if (product != null && product.getImage2() != null) {
			response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
			response.getOutputStream().write(product.getImage2());
		}
		response.getOutputStream().close();
	}

//	 hình ảnh 3
	@RequestMapping(value = { "/productImage3" }, method = RequestMethod.GET)
	public void productImage3(HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam("code") String code) throws IOException {
		Product product = null;
		if (code != null) {
			product = this.productDAO.findProduct(code);
		}
		if (product != null && product.getImage3() != null) {
			response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
			response.getOutputStream().write(product.getImage3());
		}
		response.getOutputStream().close();
	}

//	 hình ảnh 4
	@RequestMapping(value = { "/productImage4" }, method = RequestMethod.GET)
	public void productImage4(HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam("code") String code) throws IOException {
		Product product = null;
		if (code != null) {
			product = this.productDAO.findProduct(code);
		}
		if (product != null && product.getImage4() != null) {
			response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
			response.getOutputStream().write(product.getImage4());
		}
		response.getOutputStream().close();
	}

	@RequestMapping("/admin/product/product-detail")
	public String showPd_Detail(Model model, @RequestParam(value = "code", defaultValue = "") String code) {

		Product product = productDAO.findProduct(code);

//		System.out.println("Code: " + product.getCode());
//		System.out.println("Name: " + product.getName());
//		System.out.println("Price: " + product.getPrice());
//		System.out.println("Discount: " + product.getDiscount());
//		System.out.println("Discription: " + product.getDiscription());
//		System.out.println("Sort_discription: " + product.getSort_discription());
//		System.out.println("Status: " + product.getStatus());

		model.addAttribute("product_code", product.getCode());
		model.addAttribute("product_name", product.getName());
		model.addAttribute("product_price", product.getPrice());
		model.addAttribute("product_discount", product.getDiscount());
		model.addAttribute("product_discription", product.getDiscription());
		model.addAttribute("product_shortdiscription", product.getSort_discription());
		model.addAttribute("product_status", product.getStatus());
		model.addAttribute("product_amount", product.getAmount());
		model.addAttribute("product_discount", product.getDiscount());

		return "product/DetailPro";
	}

}
