package com.bigdeal.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bigdeal.constants.Consts;
import com.bigdeal.dao.OrderDAO;
import com.bigdeal.dao.ReportDAO;
import com.bigdeal.entity.OrderDetail;
import com.bigdeal.model.OrderDetailInfo;
import com.bigdeal.model.OrderInfo;
import com.bigdeal.pagination.PaginationResult;
import com.restfb.types.Payment.Item;

@Controller
@Transactional
public class ReportController {
	@Autowired
	private ReportDAO reportDAO;

	@Autowired
	private OrderDAO orderDAO;

	// Danh sách sản phẩm.
	@RequestMapping({ "/admin/report/product" })
	public String report(Model model) {
		List<OrderDetailInfo> result = new ArrayList<OrderDetailInfo>();
		List<OrderDetailInfo> listOrderDetail = orderDAO.getAllOrderDetail();
		Map<Object, Object> map =listOrderDetail.stream().collect(Collectors.groupingBy(OrderDetailInfo::getProductCode)).entrySet().stream()
				.collect(Collectors.toMap(x -> {
					int sumQuanity = x.getValue().stream().mapToInt(OrderDetailInfo::getQuanity).sum();
					double sumAmount = x.getValue().stream().mapToDouble(OrderDetailInfo::getAmount).sum();
					return new OrderDetailInfo(x.getKey(), sumQuanity, sumAmount);
				}, Map.Entry::getValue));
		for (Entry<Object, Object> entry : map.entrySet()) {
			OrderDetailInfo orderDetailInfo = (OrderDetailInfo)entry.getKey();
			result.add(orderDetailInfo);
			System.out.println("Key : " + orderDetailInfo.getProductCode());
			System.out.println("Quanity : " + orderDetailInfo.getQuanity());
			System.out.println("Amount : " + orderDetailInfo.getAmount());
		}
		model.addAttribute("listOrderDetail", result);

		return "report/index";
	}
	
	@RequestMapping(value = { "/admin/report/profit" }, method = RequestMethod.GET)
	public String orderList(Model model) {
		List<OrderInfo> result = new ArrayList<OrderInfo>();
		List<OrderInfo> paginationResult //
				= orderDAO.getAllOrder();
		Map<Object, Object> map = paginationResult.stream().collect(Collectors.groupingBy(a -> a.getOrderDate().getMonth())).entrySet().stream()
				.collect(Collectors.toMap(x -> {
					double sumAmount = x.getValue().stream().mapToDouble(OrderInfo::getAmount).sum();
					return new OrderInfo(x.getKey(), sumAmount);
				}, Map.Entry::getValue));
	
		for (Entry<Object, Object> entry : map.entrySet()) {
			System.out.println(entry.getValue());
			OrderInfo orderDetailInfo = (OrderInfo)entry.getKey();
			result.add(orderDetailInfo);
			
		}
		model.addAttribute("results", result);
		return "report/index2";
	}
	
	
	@RequestMapping(value = { "/admin/report/year" }, method = RequestMethod.GET)
	public String orderyear(Model model) {
		List<OrderInfo> resultyear = new ArrayList<OrderInfo>();
		List<OrderInfo> paginationResult //
				= orderDAO.getAllOrder();
		Map<Object, Object> map = paginationResult.stream().collect(Collectors.groupingBy(a -> a.getOrderDate().getYear())).entrySet().stream()
				.collect(Collectors.toMap(x -> {
					double sumAmount = x.getValue().stream().mapToDouble(OrderInfo::getAmount).sum();
					return new OrderInfo(x.getKey(), sumAmount);
				}, Map.Entry::getValue));
		for (Entry<Object, Object> entry : map.entrySet()) {
			OrderInfo orderDetailInfo = (OrderInfo)entry.getKey();
			resultyear.add(orderDetailInfo);
			System.out.println("Amount : " + orderDetailInfo.getAmount());
		}
		model.addAttribute("resultyear", resultyear);
		return "report/index3";
	}
	
	
	
}
