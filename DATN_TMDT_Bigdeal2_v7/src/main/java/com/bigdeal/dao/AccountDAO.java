package com.bigdeal.dao;

import java.io.IOException;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bigdeal.entity.Account;
import com.bigdeal.form.AccountForm;
import com.bigdeal.model.AccountModel;
import com.bigdeal.pagination.PaginationResult;

@Transactional
@Repository
public class AccountDAO {

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public Account findAccount(String userName) {
		Session session = this.sessionFactory.getCurrentSession();
		return session.find(Account.class, userName);
	}

	public Account findById(String userName) {
		try {
			String sql = "Select e from " + Account.class.getName() + " e Where e.userName =:id ";

			Session session = this.sessionFactory.getCurrentSession();
			Query<Account> query = session.createQuery(sql, Account.class);
			query.setParameter("id", userName);
			return (Account) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void save(AccountForm form) {

		Session session = this.sessionFactory.getCurrentSession();
		String userName = form.getUserName();

		Account item = null;

		boolean isNew = false;
		if (userName != null) {
			item = this.findById(userName);
//			form.setNewMode(true);
		}
		if (item == null) {
			isNew = true;
			item = new Account();
			item.setUserName(form.getUserName());
			form.setActive(true);

		}

		item.setEmail(form.getEmail());
		item.setEncrytedPassword(passwordEncoder.encode(form.getPassword()));
		item.setUserRole(form.getUserRole());
		item.setActive(form.isActive());

		if (form.getFileAvatar() != null) {
			byte[] image = null;
			try {
				image = form.getFileAvatar().getBytes();
			} catch (IOException e) {
			}
			if (image != null && image.length > 0) {
				item.setAvatar(image);
			}
		}

		if (isNew) {
			session.persist(item);
		}
		// Nếu có lỗi tại DB, ngoại lệ sẽ ném ra ngay lập tức
		session.flush();
	}

	public PaginationResult<AccountModel> query(int page, int maxResult, int maxNavigationPage, String likeName) {
		String sql = "Select new " + AccountModel.class.getName() //
				+ "(p.userName, p.userRole, p.active, p.email) " + " from "//
				+ Account.class.getName() + " p ";
		if (likeName != null && likeName.length() > 0) {
			sql += " Where lower(p.userName) like :likeName ";
		}
		sql += " order by p.userName desc ";
		//
		Session session = this.sessionFactory.getCurrentSession();
		Query<AccountModel> query = session.createQuery(sql, AccountModel.class);

		if (likeName != null && likeName.length() > 0) {
			query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
		}
		return new PaginationResult<AccountModel>(query, page, maxResult, maxNavigationPage);
	}

	public PaginationResult<AccountModel> query(int page, int maxResult, int maxNavigationPage) {
		return query(page, maxResult, maxNavigationPage, null);
	}

	public void delete(String id) {
		Session session = this.sessionFactory.getCurrentSession();
		Account item = this.findById(id);
		if (item != null) {
			session.delete(item);
		}
	}
	
	//hàm update sử dụng khi update 1 trường không cần quan tâm đến các trường khác
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void Update(String username,String password) {

		Session session = this.sessionFactory.getCurrentSession();// khởi động ứng dụng và lưu giữ để sử dụng sau này.
		
			Account account = findAccount(username);
	
			
//			account.setUserName(username);
			
			account.setEncrytedPassword(passwordEncoder.encode(password));
			
			session.update(account);
		
		// Nếu có lỗi tại DB, ngoại lệ sẽ ném ra ngay lập tức
		session.flush();
	}
	
	public List<Account> findAll() {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			String sql = "Select e from " + Account.class.getName() + " e ";
			 Query q = session.createQuery(sql);
			 List<Account> acountall = q.list();
			return acountall;
		} catch (NoResultException e) {
			return null;
		}
	}

}
