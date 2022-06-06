package com.bigdeal.dao;

import java.util.Date;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bigdeal.entity.Contact;
import com.bigdeal.form.ContactForm;
import com.bigdeal.model.ContactModel;
import com.bigdeal.pagination.PaginationResult;

@Transactional
@Repository
public class ContactDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public Contact findById(Long id) {
		try {
			String sql = "Select e from " + Contact.class.getName() + " e Where e.id =:id ";

			Session session = this.sessionFactory.getCurrentSession();
			Query<Contact> query = session.createQuery(sql, Contact.class);
			query.setParameter("id", id);
			return (Contact) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void save(ContactForm form) {

		Session session = this.sessionFactory.getCurrentSession();
		Long id = form.getId();

		Contact item = null;

		boolean isNew = false;
		if (id != null) {
			item = this.findById(id);
		}
		if (item == null) {
			isNew = true;
			item = new Contact();
			item.setCreatedAt(new Date());
			item.setUpdatedAt(new Date());
		} else {
			item.setId(id);
			item.setUpdatedAt(new Date());
		}

		item.setName(form.getName());
		item.setEmail(form.getEmail());
		item.setPhoneNumber(form.getPhoneNumber());
		item.setSubject(form.getSubject());
		item.setMessage(form.getMessage());
		if (isNew) {
			session.persist(item);
		}
		// Nếu có lỗi tại DB, ngoại lệ sẽ ném ra ngay lập tức
		session.flush();
	}

	public PaginationResult<ContactModel> query(int page, int maxResult, int maxNavigationPage, String likeName) {
		String sql = "Select new " + ContactModel.class.getName() //
				+ "(p.id, p.name, p.email, p.phoneNumber, p.subject, p.message) " + " from "//
				+ Contact.class.getName() + " p ";
		if (likeName != null && likeName.length() > 0) {
			sql += " Where lower(p.name) like :likeName ";
		}
		sql += " order by p.id desc ";
		//
		Session session = this.sessionFactory.getCurrentSession();
		Query<ContactModel> query = session.createQuery(sql, ContactModel.class);

		if (likeName != null && likeName.length() > 0) {
			query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
		}
		return new PaginationResult<ContactModel>(query, page, maxResult, maxNavigationPage);
	}

	public PaginationResult<ContactModel> query(int page, int maxResult, int maxNavigationPage) {
		return query(page, maxResult, maxNavigationPage, null);
	}

	public void delete(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Contact item = this.findById(id);
		if (item != null) {
			session.delete(item);
		}
	}

}