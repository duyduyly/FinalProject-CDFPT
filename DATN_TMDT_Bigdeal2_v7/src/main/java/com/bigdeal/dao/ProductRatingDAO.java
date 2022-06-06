package com.bigdeal.dao;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bigdeal.entity.Product;
import com.bigdeal.entity.ProductRating;
import com.bigdeal.form.ProductForm;
import com.bigdeal.form.ProductRatingForm;

@Transactional
@Repository
public class ProductRatingDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public List<ProductRating> findByProductCode(String code) {
		try {
			String sql = "Select e from " + ProductRating.class.getName() + " e Where e.productCode =:code ";

			Session session = this.sessionFactory.getCurrentSession();
			Query<ProductRating> query = session.createQuery(sql, ProductRating.class);
			query.setParameter("code", code);
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

//	public ProductInfo findProductInfo(String code) {
//		Product product = this.findProduct(code);
//		if (product == null) {
//			return null;
//		}
//		return new ProductInfo(product.getCode(), product.getName(), product.getPrice(), product.getCategoryId(),
//				product.getBrandId());
//	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void save(ProductRatingForm productForm) {

		Session session = this.sessionFactory.getCurrentSession();
		String code = productForm.getProductCode();

		ProductRating productRating = new ProductRating();
		productRating.setCreatedAt(new Date());
		boolean isNew = true;

		productRating.setProductCode(code);
		productRating.setMessage(productForm.getMessage());
		productRating.setRating(productForm.getRating());
		productRating.setUserName(productForm.getUserName());
		if (isNew) {
			session.persist(productRating);
		}
		// Nếu có lỗi tại DB, ngoại lệ sẽ ném ra ngay lập tức
		session.flush();
	}

//	public PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage,
//			String likeName) {
//		String sql = "Select new " + ProductInfo.class.getName() //
//				+ "(p.code, p.name, p.price, p.categoryId, p.brandId) " + " from "//
//				+ Product.class.getName() + " p ";
//		if (likeName != null && likeName.length() > 0) {
//			sql += " Where lower(p.name) like :likeName ";
//		}
//		sql += " order by p.code desc ";
//		//
//		Session session = this.sessionFactory.getCurrentSession();
//		Query<ProductInfo> query = session.createQuery(sql, ProductInfo.class);
//
//		if (likeName != null && likeName.length() > 0) {
//			query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
//		}
//		return new PaginationResult<ProductInfo>(query, page, maxResult, maxNavigationPage);
//	}
//
//	public PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage) {
//		return queryProducts(page, maxResult, maxNavigationPage, null);
//	}
//
//	public void delete(String code) {
//		Session session = this.sessionFactory.getCurrentSession();
//		Product product = this.findProduct(code);
//		if (product != null) {
//			session.delete(product);
//		}
//	}

}