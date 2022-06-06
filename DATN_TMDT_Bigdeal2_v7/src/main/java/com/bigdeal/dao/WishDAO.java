package com.bigdeal.dao;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bigdeal.entity.Blogs;
import com.bigdeal.entity.Wish;
import com.bigdeal.form.BlogForm;
import com.bigdeal.form.WishForm;
import com.bigdeal.model.BlogModel;
import com.bigdeal.pagination.PaginationResult;

@Transactional
@Repository
public class WishDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public Wish findById(Long id) {
		try {
			String sql = "Select e from " + Wish.class.getName() + " e Where e.id =:id ";

			Session session = this.sessionFactory.getCurrentSession();
			Query<Wish> query = session.createQuery(sql, Wish.class);
			query.setParameter("id", id);
			return (Wish) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Wish> findByUsername(String userName) {
		try {
			String sql = "Select e from " + Wish.class.getName() + " e Where e.userName =:userName ";

			Session session = this.sessionFactory.getCurrentSession();
			Query<Wish> query = session.createQuery(sql, Wish.class);
			query.setParameter("userName", userName);
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void save(WishForm form) {

		Session session = this.sessionFactory.getCurrentSession();
		Long id = form.getId();

		Wish item = null;

		boolean isNew = false;
		if (id != null) {
			item = this.findById(id);
		}
		if (item == null) {
			isNew = true;
			item = new Wish();
			item.setCreatedAt(new Date());
			item.setUpdatedAt(new Date());
		} else {
			item.setId(id);
			item.setUpdatedAt(new Date());
		}

		item.setProductCode(form.getProductCode());
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = "";
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		item.setUserName(username);
		if (isNew) {
			session.persist(item);
		}
		// Nếu có lỗi tại DB, ngoại lệ sẽ ném ra ngay lập tức
		session.flush();
	}

	public void delete(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Wish item = this.findById(id);
		if (item != null) {
			session.delete(item);
		}
	}

}