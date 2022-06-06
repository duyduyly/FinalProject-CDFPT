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

import com.bigdeal.entity.Banner;
import com.bigdeal.entity.Blogs;
import com.bigdeal.form.BannerForm;
import com.bigdeal.form.BlogForm;
import com.bigdeal.model.BlogModel;
import com.bigdeal.pagination.PaginationResult;

@Transactional
@Repository
public class BannerDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public Banner findById(Long id) {
		try {
			String sql = "Select e from " + Banner.class.getName() + " e Where e.id =:id ";

			Session session = this.sessionFactory.getCurrentSession();
			Query<Banner> query = session.createQuery(sql, Banner.class);
			query.setParameter("id", id);
			return (Banner) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Banner> findByPosition(int position) {
		try {
			String sql = "Select e from " + Banner.class.getName() + " e Where e.position =:position ";

			Session session = this.sessionFactory.getCurrentSession();
			Query<Banner> query = session.createQuery(sql, Banner.class);
			query.setParameter("position", position);
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Banner> findAll() {
		try {
			String sql = "Select e from " + Banner.class.getName() + " e ";

			Session session = this.sessionFactory.getCurrentSession();
			Query<Banner> query = session.createQuery(sql, Banner.class);
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void save(BannerForm form) {

		Session session = this.sessionFactory.getCurrentSession();
		Long id = form.getId();

		Banner item = null;

		boolean isNew = false;
		if (id != null) {
			item = this.findById(id);
		}
		if (item == null) {
			isNew = true;
			item = new Banner();
			item.setCreatedAt(new Date());
			item.setUpdatedAt(new Date());
		} else {
			item.setId(id);
			item.setUpdatedAt(new Date());
		}

		item.setContent(form.getContent());
		item.setCategoryId(form.getCategoryId());
		item.setPosition(form.getPosition());

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
		if (form.getFileData2() != null) {
			byte[] image2 = null;
			try {
				image2 = form.getFileData2().getBytes();
			} catch (IOException e) {
			}
			if (image2 != null && image2.length > 0) {
				item.setImage2(image2);
			}
		}
		if (isNew) {
			session.persist(item);
		}
		// Nếu có lỗi tại DB, ngoại lệ sẽ ném ra ngay lập tức
		session.flush();
	}

//	public PaginationResult<BlogModel> query(int page, int maxResult, int maxNavigationPage, String likeName) {
//		String sql = "Select new " + BlogModel.class.getName() //
//				+ "(p.id, p.title, p.shortDescription, p.description) " + " from "//
//				+ Blogs.class.getName() + " p ";
//		if (likeName != null && likeName.length() > 0) {
//			sql += " Where lower(p.title) like :likeName ";
//		}
//		sql += " order by p.id desc ";
//		//
//		Session session = this.sessionFactory.getCurrentSession();
//		Query<BlogModel> query = session.createQuery(sql, BlogModel.class);
//
//		if (likeName != null && likeName.length() > 0) {
//			query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
//		}
//		return new PaginationResult<BlogModel>(query, page, maxResult, maxNavigationPage);
//	}
//
//	public PaginationResult<BlogModel> query(int page, int maxResult, int maxNavigationPage) {
//		return query(page, maxResult, maxNavigationPage, null);
//	}

	public void delete(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Banner item = this.findById(id);
		if (item != null) {
			session.delete(item);
		}
	}

}