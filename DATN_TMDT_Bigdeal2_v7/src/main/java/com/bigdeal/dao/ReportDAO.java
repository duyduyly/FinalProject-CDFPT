package com.bigdeal.dao;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bigdeal.entity.Categories;
import com.bigdeal.entity.Order;
import com.bigdeal.entity.OrderDetail;
import com.bigdeal.entity.Product;
import com.bigdeal.model.CartInfo;
import com.bigdeal.model.CartLineInfo;
import com.bigdeal.model.CustomerInfo;
import com.bigdeal.model.OrderDetailInfo;
import com.bigdeal.model.OrderInfo;
import com.bigdeal.model.ReportModel;
import com.bigdeal.pagination.PaginationResult;

@Transactional
@Repository
public class ReportDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private ProductDAO productDAO;


	

}