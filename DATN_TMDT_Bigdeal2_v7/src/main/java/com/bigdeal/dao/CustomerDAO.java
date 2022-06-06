package com.bigdeal.dao;

import java.io.IOException;
import java.util.Date;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bigdeal.entity.Blogs;
import com.bigdeal.entity.Customers;
import com.bigdeal.form.BlogForm;
import com.bigdeal.form.CustomerForm2;
import com.bigdeal.model.BlogModel;
import com.bigdeal.model.CustomerModel;
import com.bigdeal.pagination.PaginationResult;

@Transactional
@Repository
public class CustomerDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public Customers findById(Long id) {
		try {
			String sql = "Select e from " + Customers.class.getName() + " e Where e.id =:id ";

			Session session = this.sessionFactory.getCurrentSession();
			Query<Customers> query = session.createQuery(sql, Customers.class);
			query.setParameter("id", id);
			return (Customers) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void save(CustomerForm2 form) {

		Session session = this.sessionFactory.getCurrentSession();
		Long id = form.getId();

		Customers item = null;

		boolean isNew = false;
		if (id != null) {
			item = this.findById(id);
		}
		if (item == null) {
			isNew = true;
			item = new Customers();
			item.setCreatedAt(new Date());
			item.setUpdatedAt(new Date());
		} else {
			item.setId(id);
			item.setUpdatedAt(new Date());
		}

		item.setCustomerName(form.getCustomerName());
		item.setEmail(form.getEmail());
		item.setPhoneNumber(form.getPhoneNumber());
		item.setAddress(form.getAddress());
		item.setGender(form.getGender());
		item.setWardId(form.getWardId());
		item.setDistrictId(form.getDistrictId());
		item.setCityId(form.getCityId());
//		if (form.getFileData() != null) {
//			byte[] image = null;
//			try {
//				image = form.getFileData().getBytes();
//			} catch (IOException e) {
//			}
//			if (image != null && image.length > 0) {
//				item.setImage(image);
//			}
//		}
		if (isNew) {
			session.persist(item);
		}
		// Nếu có lỗi tại DB, ngoại lệ sẽ ném ra ngay lập tức
		session.flush();
	}

	public PaginationResult<CustomerModel> query(int page, int maxResult, int maxNavigationPage, String likeName) {
		String sql = "Select new " + CustomerModel.class.getName() //
				+ "(p.id, p.customerName, p.email, p.phoneNumber, p.address, p.gender) " + " from "//
				+ Customers.class.getName() + " p ";
		if (likeName != null && likeName.length() > 0) {
			sql += " Where lower(p.customerName) like :likeName ";
		}
		sql += " order by p.id desc ";
		//
		Session session = this.sessionFactory.getCurrentSession();
		Query<CustomerModel> query = session.createQuery(sql, CustomerModel.class);

		if (likeName != null && likeName.length() > 0) {
			query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
		}
		return new PaginationResult<CustomerModel>(query, page, maxResult, maxNavigationPage);
	}

	public PaginationResult<CustomerModel> query(int page, int maxResult, int maxNavigationPage) {
		return query(page, maxResult, maxNavigationPage, null);
	}

	public void delete(Long id) {
		Session session = this.sessionFactory.getCurrentSession();
		Customers item = this.findById(id);
		if (item != null) {
			session.delete(item);
		}
	}

}