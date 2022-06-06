package com.bigdeal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Service;

import com.bigdeal.constants.Consts;
import com.bigdeal.dao.AccountDAO;
import com.bigdeal.form.AccountForm;

@Service
public class FacebookConnectionSignup implements ConnectionSignUp {

	@Autowired
	private AccountDAO accountDAO;
	@Autowired
	PasswordEncoder passwordEncoder;
//đăng ký user facebook
	@Override
	public String execute(Connection<?> connection) {
		System.out.println("signup ===> ");
		final AccountForm user = new AccountForm();
		user.setUserName(connection.getDisplayName());
		user.setPassword(passwordEncoder.encode("123456"));
		user.setUserRole(Consts.ROLE_USER);
		accountDAO.save(user);
		return user.getUserName();
	}

}
