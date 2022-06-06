package com.bigdeal.dao;

import java.io.IOException;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bigdeal.entity.Banner;
import com.bigdeal.entity.Blogs;
import com.bigdeal.entity.Categories;
import com.bigdeal.entity.Order;
import com.bigdeal.entity.OrderDetail;
import com.bigdeal.entity.Product;
import com.bigdeal.form.BannerForm;
import com.bigdeal.model.CartInfo;
import com.bigdeal.model.CartLineInfo;
import com.bigdeal.model.CustomerInfo;
import com.bigdeal.model.OrderDetailInfo;
import com.bigdeal.model.OrderInfo;
import com.bigdeal.pagination.PaginationResult;

@Transactional
@Repository
public class OrderDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private ProductDAO productDAO;

	private int getMaxOrderNum() {
		String sql = "Select max(o.orderNum) from " + Order.class.getName() + " o ";
		Session session = this.sessionFactory.getCurrentSession();
		Query<Integer> query = session.createQuery(sql, Integer.class);
		Integer value = (Integer) query.getSingleResult();
		if (value == null) {
			return 0;
		}
		return value;
	}

	@Transactional(rollbackFor = Exception.class)
	public void saveOrder(CartInfo cartInfo) {
		Session session = this.sessionFactory.getCurrentSession();

		int orderNum = this.getMaxOrderNum() + 1;
		Order order = new Order();

		order.setId(UUID.randomUUID().toString());
		order.setOrderNum(orderNum);
		order.setOrderDate(new Date());
		order.setAmount(cartInfo.getAmountTotal());

		CustomerInfo customerInfo = cartInfo.getCustomerInfo();
		order.setCustomerName(customerInfo.getName());
		order.setCustomerEmail(customerInfo.getEmail());
		order.setCustomerPhone(customerInfo.getPhone());
		order.setCustomerAddress(customerInfo.getAddress());

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = "";
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		order.setUserName(username);
		order.setStatus(1);
		session.persist(order);

		List<CartLineInfo> lines = cartInfo.getCartLines();

		for (CartLineInfo line : lines) {
			OrderDetail detail = new OrderDetail();
			detail.setId(UUID.randomUUID().toString());
			detail.setOrder(order);
			detail.setAmount(line.getAmount());
			detail.setPrice(line.getProductInfo().getPrice());
			detail.setQuanity(line.getQuantity());

			String code = line.getProductInfo().getCode();
			Product product = this.productDAO.findProduct(code);
			detail.setProduct(product);

			session.persist(detail);
		}

		// Order Number!
		cartInfo.setOrderNum(orderNum);
		// Flush
		session.flush();
	}

	// @page = 1, 2, ...
	public PaginationResult<OrderInfo> listOrderInfo(int page, int maxResult, int maxNavigationPage) {
		
			String sql = "Select new " + OrderInfo.class.getName()//
					+ "(ord.id, ord.orderDate, ord.orderNum, ord.amount, "
					+ " ord.customerName, ord.customerAddress, ord.customerEmail, ord.customerPhone, ord.userName, ord.status) "
					+ " from " + Order.class.getName() + " ord "//
					+ " order by ord.orderNum desc";

			Session session = this.sessionFactory.getCurrentSession();
			Query<OrderInfo> query = session.createQuery(sql, OrderInfo.class);
			return new PaginationResult<OrderInfo>(query, page, maxResult, maxNavigationPage);
	
	}

	public List<OrderInfo> getAllOrder() {
		String sql = "Select new " + OrderInfo.class.getName()//
				+ "(ord.id, ord.orderDate, ord.orderNum, ord.amount, "
				+ " ord.customerName, ord.customerAddress, ord.customerEmail, ord.customerPhone, ord.userName, ord.status) "
				+ " from " + Order.class.getName() + " ord "//
				+ " order by ord.orderNum desc";

		Session session = this.sessionFactory.getCurrentSession();
		Query<OrderInfo> query = session.createQuery(sql, OrderInfo.class);
		return query.getResultList();
	}

	public Order findOrder(String orderId) {
		Session session = this.sessionFactory.getCurrentSession();
		return session.find(Order.class, orderId);
	}

	public OrderInfo getOrderInfo(String orderId) {
		Order order = this.findOrder(orderId);
		if (order == null) {
			return null;
		}
		return new OrderInfo(order.getId(), order.getOrderDate(), //
				order.getOrderNum(), order.getAmount(), order.getCustomerName(), //
				order.getCustomerAddress(), order.getCustomerEmail(), order.getCustomerPhone(), order.getUserName(),
				order.getStatus());
	}

	public List<Order> findByUsername(String userName) {
		try {
			String sql = "Select e from " + Order.class.getName() + " e Where e.userName =:userName ";

			Session session = this.sessionFactory.getCurrentSession();
			Query<Order> query = session.createQuery(sql, Order.class);
			query.setParameter("userName", userName);
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<OrderDetailInfo> listOrderDetailInfos(String orderId) {
		String sql = "Select new " + OrderDetailInfo.class.getName() //
				+ "(d.id, d.product.code, d.product.name , d.quanity,d.price,d.amount) "//
				+ " from " + OrderDetail.class.getName() + " d "//
				+ " where d.order.id = :orderId ";

		Session session = this.sessionFactory.getCurrentSession();
		Query<OrderDetailInfo> query = session.createQuery(sql, OrderDetailInfo.class);
		query.setParameter("orderId", orderId);

		return query.getResultList();
	}

	public List<OrderDetailInfo> getAllOrderDetail() {
		String sql = "Select new " + OrderDetailInfo.class.getName() //
				+ "(d.id, d.product.code, d.product.name , d.quanity,d.price,d.amount) "//
				+ " from " + OrderDetail.class.getName() + " d " + " order by d.product.code";//

		Session session = this.sessionFactory.getCurrentSession();
		Query<OrderDetailInfo> query = session.createQuery(sql, OrderDetailInfo.class);

		return query.getResultList();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void save(int orderNum) {

		Session session = this.sessionFactory.getCurrentSession();

		Order item = this.findById(orderNum);
		item.setStatus(2);

		// Nếu có lỗi tại DB, ngoại lệ sẽ ném ra ngay lập tức
		session.flush();
	}

	public Order findById(int id) {
		try {
			String sql = "Select e from " + Order.class.getName() + " e Where e.orderNum =:id ";

			Session session = this.sessionFactory.getCurrentSession();
			Query<Order> query = session.createQuery(sql, Order.class);
			query.setParameter("id", id);
			return (Order) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	public Order findByoder(int id) {
		try {
			String sql = "Select e from " + Order.class.getName() + " e Where e.orderNum =:id ";

			Session session = this.sessionFactory.getCurrentSession();
			Query<Order> query = session.createQuery(sql, Order.class);
			query.setParameter("id", id);
			return (Order) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Order findByOderId(String id) {
		try {
			String sql = "Select e from " + Order.class.getName() + " e Where e.id =:id ";

			Session session = this.sessionFactory.getCurrentSession();
			Query<Order> query = session.createQuery(sql, Order.class);
			query.setParameter("id", id);
			return (Order) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public void delete(String id) {
		Session session = this.sessionFactory.getCurrentSession();
		Order item = this.findByOderId(id);
		if (item != null) {
			session.delete(item);
		}
	}
}