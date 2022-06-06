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

import com.bigdeal.entity.Blogs;
import com.bigdeal.form.BlogForm;
import com.bigdeal.model.BlogModel;
import com.bigdeal.pagination.PaginationResult;

@Transactional
@Repository
public class BlogDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public Blogs findById(Long id) {
		try {
			String sql = "Select e from " + Blogs.class.getName() + " e Where e.id =:id ";

			Session session = this.sessionFactory.getCurrentSession();
			Query<Blogs> query = session.createQuery(sql, Blogs.class);
			query.setParameter("id", id);
			return (Blogs) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Blogs> findAll() {
		try {
			String sql = "Select e from " + Blogs.class.getName() + " e ";

			Session session = this.sessionFactory.getCurrentSession();
			Query<Blogs> query = session.createQuery(sql, Blogs.class);
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Blogs findBrandInfo(Long id) {
		Blogs item = this.findById(id);
		if (item == null) {
			return null;
		}
		return new Blogs(item.getTitle(), item.getShortDescription(), item.getDescription(), item.getImage(),
				item.getCategoryId(), item.getDeletedAt(), item.getCreatedAt(), item.getUpdatedAt());
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void save(BlogForm form) {

		Session session = this.sessionFactory.getCurrentSession();
		Long id = form.getId();

		Blogs item = null;

		boolean isNew = false;
		if (id != null) {
			item = this.findById(id);
		}
		if (item == null) {
			isNew = true;
			item = new Blogs();
			item.setCreatedAt(new Date());
			item.setUpdatedAt(new Date());
		} else {
			item.setId(id);
			item.setUpdatedAt(new Date());
		}

		item.setTitle(form.getTitle());
		item.setShortDescription(form.getShortDescription());
		item.setDescription(form.getDescription());

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

	public PaginationResult<BlogModel> query(int page, int maxResult, int maxNavigationPage, String likeName) {
		String sql = "Select new " + BlogModel.class.getName() //
				+ "(p.id, p.title, p.shortDescription, p.description) " + " from "//
				+ Blogs.class.getName() + " p ";
		if (likeName != null && likeName.length() > 0) {
			sql += " Where lower(p.title) like :likeName ";
		}
		sql += " order by p.id desc ";
		//
		Session session = this.sessionFactory.getCurrentSession();
		Query<BlogModel> query = session.createQuery(sql, BlogModel.class);

		if (likeName != null && likeName.length() > 0) {
			query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
		}
		return new PaginationResult<BlogModel>(query, page, maxResult, maxNavigationPage);
	}

	public PaginationResult<BlogModel> query(int page, int maxResult, int maxNavigationPage) {
		return query(page, maxResult, maxNavigationPage, null);
	}

	public void delete(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Blogs item = this.findById(id);
		if (item != null) {
			session.delete(item);
		}
	}

}