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

import com.bigdeal.entity.Categories;
import com.bigdeal.form.CategoryForm;
import com.bigdeal.model.CategoryInfo;
import com.bigdeal.pagination.PaginationResult;

@Transactional
@Repository
public class CategoryDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public Categories findById(Long id) {
		try {
			String sql = "Select e from " + Categories.class.getName() + " e Where e.id =:id ";

			Session session = this.sessionFactory.getCurrentSession();
			Query<Categories> query = session.createQuery(sql, Categories.class);
			query.setParameter("id", id);
			return (Categories) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Categories> findAll() {
		try {
			String sql = "Select e from " + Categories.class.getName() + " e ";

			Session session = this.sessionFactory.getCurrentSession();
			Query<Categories> query = session.createQuery(sql, Categories.class);
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	public CategoryInfo findProductInfo(Long id) {
		Categories category = this.findById(id);
		if (category == null) {
			return null;
		}
		return new CategoryInfo(category.getCategoryName(), category.getImage(), category.getDescription(),
				category.getDeletedAt(), category.getCreatedAt(), category.getUpdatedAt());
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void save(CategoryForm form) {

		Session session = this.sessionFactory.getCurrentSession();
		Long id = form.getId();

		Categories category = null;

		boolean isNew = false;
		if (id != null) {
			category = this.findById(id);
		}
		if (category == null) {
			isNew = true;
			category = new Categories();
			category.setCreatedAt(new Date());
			category.setUpdatedAt(new Date());
		} else {
			category.setId(id);
		}

		category.setCategoryName(form.getCategoryName());
		category.setDescription(form.getDescription());

		if (form.getFileData() != null) {
			byte[] image = null;
			try {
				image = form.getFileData().getBytes();
			} catch (IOException e) {
			}
			if (image != null && image.length > 0) {
				category.setImage(image);
			}
		}
		if (isNew) {
			session.persist(category);
		}
		// Nếu có lỗi tại DB, ngoại lệ sẽ ném ra ngay lập tức
		session.flush();
	}

	public PaginationResult<CategoryInfo> query(int page, int maxResult, int maxNavigationPage, String likeName) {
		String sql = "Select new " + CategoryInfo.class.getName() //
				+ "(p.id, p.categoryName, p.description) " + " from "//
				+ Categories.class.getName() + " p ";
		if (likeName != null && likeName.length() > 0) {
			sql += " Where lower(p.categoryName) like :likeName ";
		}
		sql += " order by p.id desc ";
		//
		Session session = this.sessionFactory.getCurrentSession();
		Query<CategoryInfo> query = session.createQuery(sql, CategoryInfo.class);

		if (likeName != null && likeName.length() > 0) {
			query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
		}
		return new PaginationResult<CategoryInfo>(query, page, maxResult, maxNavigationPage);
	}

	public PaginationResult<CategoryInfo> query(int page, int maxResult, int maxNavigationPage) {
		return query(page, maxResult, maxNavigationPage, null);
	}

	public void delete(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Categories category = this.findById(id);
		if (category != null) {
			session.delete(category);
		}
	}

}