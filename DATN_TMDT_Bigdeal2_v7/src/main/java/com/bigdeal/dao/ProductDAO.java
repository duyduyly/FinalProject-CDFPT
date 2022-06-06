package com.bigdeal.dao;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.apache.commons.digester.plugins.strategies.FinderFromClass;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bigdeal.entity.Categories;
import com.bigdeal.entity.Order;
import com.bigdeal.entity.OrderDetail;
import com.bigdeal.entity.Product;
import com.bigdeal.form.ProductForm;
import com.bigdeal.model.ProductInfo;
import com.bigdeal.model.ReportModel;
import com.bigdeal.pagination.PaginationResult;

@Transactional
@Repository
public class ProductDAO {

	@Autowired
	private SessionFactory sessionFactory;

	// chi tiết sản phẩm
	public Product findProduct(String code) {
		try {
			// Query sử dụng chuỗi truy vấn SQL hoặc Hibernate Query
			// Language (HQL) để lấy dữ liệu từ cơ sở dữ liệu và tạo các đối tượng

			String sql = "Select e from " + Product.class.getName() + " e Where e.code =:code ";

			Session session = this.sessionFactory.getCurrentSession();
			// createQuery You can find the size of a collection without initializing it

			Query<Product> query = session.createQuery(sql, Product.class);
			query.setParameter("code", code);
			return (Product) query.getSingleResult(); // sử dụng xử lý ngoại lệ khi không có giá trị
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Product> findProductdemo(String code) {

		try {
			String sql = "Select e from " + Product.class.getName() + " e Where e.code =:code ";

			Session session = this.sessionFactory.getCurrentSession();
			Query<Product> query = session.createQuery(sql, Product.class);
			query.setParameter("code", code);
			List<Product> l = query.list();
			return l;
		} catch (NoResultException e) {
			return null;
		}

	}

	public PaginationResult<Product> findAll(int page, int maxResult, int maxNavigationPage) {
		try {
			String sql = "Select e from " + Product.class.getName() + " e ";

			Session session = this.sessionFactory.getCurrentSession();
			Query<Product> query = session.createQuery(sql, Product.class);
			return new PaginationResult<Product>(query, page, maxResult, maxNavigationPage);
		} catch (NoResultException e) {
			return null;
		}
	}

//	Find SPECIAL PRODUCTS
	public List<Product> findAll_sp() {
		try {
			String sql = "Select e from " + Product.class.getName() + " e ";

			Session session = this.sessionFactory.getCurrentSession();
			Query<Product> query = session.createQuery(sql, Product.class);
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	
	public List<Product> findbyBrand(Long Brandid) {
		try {
			// Query sử dụng chuỗi truy vấn SQL hoặc Hibernate Query
			// Language (HQL) để lấy dữ liệu từ cơ sở dữ liệu và tạo các đối tượng

			String sql = "Select e from " + Product.class.getName() + " e Where e.brandId =:Brandid ";
			Session session = this.sessionFactory.getCurrentSession();
			// createQuery You can find the size of a collection without initializing it
			Query<Product> query = session.createQuery(sql, Product.class);
			query.setParameter("Brandid", Brandid);
			return query.getResultList(); // sử dụng xử lý ngoại lệ khi không có giá trị
		} catch (NoResultException e) {
			return null;
		}
	}
	
	
	
	// sản phẩm theo category
	public PaginationResult<Product> findByCategory(Long categoryId, int page, int maxResult, int maxNavigationPage) {
		try {
			String sql = "Select e from " + Product.class.getName() + " e Where e.categoryId =:categoryId ";

			Session session = this.sessionFactory.getCurrentSession();
			Query<Product> query = session.createQuery(sql, Product.class);
			query.setParameter("categoryId", categoryId);
			return new PaginationResult<Product>(query, page, maxResult, maxNavigationPage);
		} catch (NoResultException e) {
			return null;
		}
	}

	// tìm kiếm theo giá category
			public PaginationResult<Product> findByPrice(double price, int page, int maxResult, int maxNavigationPage) {
				try {
					String sql = "Select e from " + Product.class.getName() + " e WHERE e.price >=(:price - 5000000.00) AND e.price <=:price";

					Session session = this.sessionFactory.getCurrentSession();
					Query<Product> query = session.createQuery(sql, Product.class);
					query.setParameter("price", price);
					return new PaginationResult<Product>(query, page, maxResult, maxNavigationPage);
				} catch (NoResultException e) {
					return null;
				}
			}
	
	
	public PaginationResult<Product> findByCategorydemo(Long categoryId, int page, int maxResult,
			int maxNavigationPage) {
		try {
			String sql = "Select e from " + Product.class.getName() + " e Where e.categoryId =:categoryId ";

			Session session = this.sessionFactory.getCurrentSession();
			Query<Product> query = session.createQuery(sql, Product.class);
			query.setParameter("categoryId", categoryId);
			return new PaginationResult<Product>(query, page, maxResult, maxNavigationPage);
		} catch (NoResultException e) {
			return null;
		}
	}

	public PaginationResult<Product> findByName(String productName, int page, int maxResult, int maxNavigationPage) {
		try {
			String sql = "Select e from " + Product.class.getName() + " e Where lower(e.name) like :likeName ";
			// Product.class.getName() gọi thẳng đến getname ko cần instane

			Session session = this.sessionFactory.getCurrentSession();
			Query<Product> query = session.createQuery(sql, Product.class);
			query.setParameter("likeName", "%" + productName.toLowerCase() + "%");
			return new PaginationResult<Product>(query, page, maxResult, maxNavigationPage);
		} catch (NoResultException e) {
			return null;
		}
	}

	
	
	public List<Product> countpd() {
		Session session = this.sessionFactory.getCurrentSession();
		String sql = "Select e from " + Product.class.getName() + " e ";
		Query q = session.createQuery(sql);

		List<Product> l = q.list();

		return l;
	}

	public List<OrderDetail> countspdb() {

		Session session = this.sessionFactory.getCurrentSession();
		String sql = "Select sum(o.quanity) from " + "OrderDetail" + " o ";
		Query q = session.createQuery(sql);

		List<OrderDetail> l = q.list();

		return l;
	}
	
	
	//tìm kiếm theo trạng thái
	public List<Product> findProduct_Status(String status) {

		try {
			String sql = "Select e from " + Product.class.getName() + " e Where e.status =:status ";

			Session session = this.sessionFactory.getCurrentSession();
			Query<Product> query = session.createQuery(sql, Product.class);
			query.setParameter("status", status);
			List<Product> l = query.list();
			return l;
		} catch (NoResultException e) {
			return null;
		}

	}

	
	// sản phẩm theo Brands
			public PaginationResult<Product> findByBrands(Long brandId, int page, int maxResult, int maxNavigationPage) {
				try {
					String sql = "Select e from " + Product.class.getName() + " e Where e.brandId =:brandId ";

					Session session = this.sessionFactory.getCurrentSession();
					Query<Product> query = session.createQuery(sql, Product.class);
					query.setParameter("brandId", brandId);
					return new PaginationResult<Product>(query, page, maxResult, maxNavigationPage);
				} catch (NoResultException e) {
					return null;
				}
			}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void save(ProductForm productForm) {

		Session session = this.sessionFactory.getCurrentSession();// khởi động ứng dụng và lưu giữ để sử dụng sau này.
		String code = productForm.getCode();

		Product product = null;

		boolean isNew = false;
		if (code != null) {
			product = this.findProduct(code);
		}
		if (product == null) {
			isNew = true;// dùng để check trạng thái input là thêm mới hay sửa dữ liệu
			product = new Product();
			product.setCreateDate(new Date());
		}
		product.setCode(code);
		product.setName(productForm.getName());
		product.setPrice(productForm.getPrice());
		product.setCategoryId(productForm.getCategoryId());
		product.setBrandId(productForm.getBrandId());
		product.setDiscount(productForm.getDiscount());
		product.setStatus("new");

		// mới
		product.setAmount(productForm.getAmount());
		product.setDiscription(productForm.getDiscription());
		product.setSort_discription(productForm.getSort_discription());
		if (productForm.getFileData() != null) {
			byte[] image = null;
			byte[] image2 = null;
			byte[] image3 = null;
			byte[] image4 = null;
			try {
				image = productForm.getFileData().getBytes();
				image2 = productForm.getFileimage2().getBytes();
				image3 = productForm.getFileimage3().getBytes();
				image4 = productForm.getFileimage4().getBytes();
				// lấy file chuyển hóa về chuỗi ký tự máy
			} catch (IOException e) {
			}
			if (image != null && image.length > 0 && image2 != null && image2.length > 0 && image3 != null
					&& image3.length > 0 && image4 != null && image4.length > 0) {
				product.setImage(image);
				product.setImage2(image2);
				product.setImage3(image3);
				product.setImage4(image4);
			}
		}
		if (isNew) {
			// persist is similar to save (with transaction) and it adds the entity object
			session.persist(product);
		}
		// Nếu có lỗi tại DB, ngoại lệ sẽ ném ra ngay lập tức
		session.flush();
	}

	
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void Update(String code,int quality) {

		Session session = this.sessionFactory.getCurrentSession();// khởi động ứng dụng và lưu giữ để sử dụng sau này.
		
			Product product = findProduct(code);
			// persist is similar to save (with transaction) and it adds the entity object
			
			product.setCode(code);
			
			product.setAmount(product.getAmount() - quality);
			session.update(product);
		
		// Nếu có lỗi tại DB, ngoại lệ sẽ ném ra ngay lập tức
		session.flush();
	}
	
	public PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage,
			String likeName) {
		String sql = "Select new " + ProductInfo.class.getName() //
				+ "(p.code, p.name, p.price, p.categoryId, p.brandId, p.discount, p.amount, p.discription, p.sort_discription, p.status) "
				+ " from "//
				+ Product.class.getName() + " p ";
		if (likeName != null && likeName.length() > 0) {
			sql += " Where lower(p.name) like :likeName ";
		}

		sql += " order by p.createDate desc ";
		//
		Session session = this.sessionFactory.getCurrentSession();
		Query<ProductInfo> query = session.createQuery(sql, ProductInfo.class);

		if (likeName != null && likeName.length() > 0) {
			query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
		}
		return new PaginationResult<ProductInfo>(query, page, maxResult, maxNavigationPage);
	}

	public PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage) {
		return queryProducts(page, maxResult, maxNavigationPage, null);
	}

	public void delete(String code) {
		Session session = this.sessionFactory.getCurrentSession();
		Product product = this.findProduct(code);
		if (product != null) {
			session.delete(product);
		}
	}

}