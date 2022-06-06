package com.bigdeal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bigdeal.constants.Consts;
import com.bigdeal.dao.OrderDAO;
import com.bigdeal.model.OrderDetailInfo;
import com.bigdeal.model.OrderInfo;
import com.bigdeal.pagination.PaginationResult;

@Controller
@Transactional
public class OrderController {
	@Autowired
	private OrderDAO orderDAO;

	@RequestMapping(value = { "/admin/orders" }, method = RequestMethod.GET)
	public String orderList(Model model, //
			@RequestParam(value = "page", defaultValue = "1") String pageStr) {
		int page = 1;
		try {
			page = Integer.parseInt(pageStr);
		} catch (Exception e) {
		}


		PaginationResult<OrderInfo> paginationResult //
				= orderDAO.listOrderInfo(page, Consts.RESULT_PER_PAGE, Consts.MAX_NAVIGATION_PAGE);

		model.addAttribute("paginationResult", paginationResult);
		return "order/index";
	}

	@RequestMapping(value = { "/admin/order" }, method = RequestMethod.GET)
	public String orderView(Model model, @RequestParam("orderId") String orderId) {
		OrderInfo orderInfo = null;
		if (orderId != null) {
			orderInfo = this.orderDAO.getOrderInfo(orderId);
		}
		if (orderInfo == null) {
			return "redirect:/admin/orderList";
		}
		List<OrderDetailInfo> details = this.orderDAO.listOrderDetailInfos(orderId);
		orderInfo.setDetails(details);

		model.addAttribute("orderInfo", orderInfo);

		return "order/view";
	}

	@RequestMapping(value = Consts.adminPath + "/order/delete", method = RequestMethod.GET)
	public String deleteUser(@RequestParam("id") String id, Model model) {
		orderDAO.delete(id);
		return "redirect:/admin/orders";
	}
}
