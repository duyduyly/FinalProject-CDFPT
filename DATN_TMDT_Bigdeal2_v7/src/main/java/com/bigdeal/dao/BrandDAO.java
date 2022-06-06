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

import com.bigdeal.entity.Brands;
import com.bigdeal.form.BrandForm;
import com.bigdeal.model.BrandInfo;
import com.bigdeal.pagination.PaginationResult;

@Transactional
@Repository
public class BrandDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public Brands findById(Long id) {
		try {
			String sql = "Select e from " + Brands.class.getName() + " e Where e.id =:id ";

			Session session = this.sessionFactory.getCurrentSession();
			Query<Brands> query = session.createQuery(sql, Brands.class);
			query.setParameter("id", id);
			return (Brands) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Brands> findAll() {
		try {
			String sql = "Select e from " + Brands.class.getName() + " e ";

			Session session = this.sessionFactory.getCurrentSession();
			Query<Brands> query = session.createQuery(sql, Brands.class);
			return query.getResultList();
		} catch (NoResultException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Brands findBrandInfo(Long id) {
		Brands item = this.findById(id);
		if (item == null) {
			return null;
		}
		return new Brands(item.getBrandName(), item.getImage(), item.getDescription(), item.getSlug(),
				 item.getCreatedAt(), item.getUpdatedAt());
	}

	public List<Brands> findByCategoryId(Long categoryId) {
		try {
			String sql = "Select e from " + Brands.class.getName() + " e Where e.categoriesId.id =:categoryId ";

			Session session = this.sessionFactory.getCurrentSession();
			Query<Brands> query = session.createQuery(sql, Brands.class);
			query.setParameter("categoryId", categoryId);
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void save(BrandForm form) {

		Session session = this.sessionFactory.getCurrentSession();
		Long id = form.getId();

		Brands item = null;

		boolean isNew = false;
		if (id != null) {
			item = this.findById(id);
		}
		if (item == null) {
			isNew = true;
			item = new Brands();
			item.setCreatedAt(new Date());
			item.setUpdatedAt(new Date());
		} else {
			item.setId(id);
			item.setUpdatedAt(new Date());
		}

		item.setBrandName(form.getBrandName());
		item.setDescription(form.getDescription());
		item.setCategoriesId(form.getCategoriesId());
		if (form.getFileData() != null) {
			byte[] image = null;
			try {
				image = form.getFileData().getBytes();
			} catch (IOException e) {
			}
			if (image != null && image.length > 0) {
				item.setImage(image);
			}
		}
		if (isNew) {
			session.persist(item);
		}
		// Nếu có lỗi tại DB, ngoại lệ sẽ ném ra ngay lập tức
		session.flush();
	}

	public PaginationResult<BrandInfo> query(int page, int maxResult, int maxNavigationPage, String likeName) {
		String sql = "Select new " + BrandInfo.class.getName() //
				+ "(p.id, p.brandName, p.description) " + " from "//
				+ Brands.class.getName() + " p ";
		if (likeName != null && likeName.length() > 0) {
			sql += " Where lower(p.brandName) like :likeName ";
		}
		sql += " order by p.id desc ";
		//
		Session session = this.sessionFactory.getCurrentSession();
		Query<BrandInfo> query = session.createQuery(sql, BrandInfo.class);

		if (likeName != null && likeName.length() > 0) {
			query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
		}
		return new PaginationResult<BrandInfo>(query, page, maxResult, maxNavigationPage);
	}

	public PaginationResult<BrandInfo> query(int page, int maxResult, int maxNavigationPage) {
		return query(page, maxResult, maxNavigationPage, null);
	}

	public void delete(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Brands item = this.findById(id);
		if (item != null) {
			session.delete(item);
		}
	}

}