package com.bigdeal.controller.frontend;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bigdeal.dao.OrderDAO;
import com.bigdeal.servlet.Config;

@Controller
public class VnpayController {

	@Autowired
	private OrderDAO orderDAO;

	@RequestMapping("/vnpay-payment")
	public String vnpay(HttpServletRequest request,
			@RequestParam(value = "amount", defaultValue = "0") double amountOrder,
			@RequestParam(value = "orderId", defaultValue = "0") int orderId) throws UnsupportedEncodingException {
		String vnp_Version = "2.0.0";// Phiên bản api mà merchant kết nối. Phiên bản hiện tại là 2.0.0
		String vnp_Command = "pay";// Mã API sử dụng, mã cho giao dịch thanh toán là pay
//		String vnp_OrderInfo = "Thanh Toán Đơn Hàng (orderId=" + orderId + ")";
		String vnp_OrderInfo = String.valueOf(orderId);
		String orderType = "billpayment";// Thông tin mô tả nội dung thanh toán (Tiếng Việt, không dấu).
		String vnp_TxnRef = Config.getRandomNumber(8);// Mã tham chiếu của giao dịch tại hệ thống của merchant.
		// Mã này là duy nhất đùng để phân biệt các đơn hàng gửi sang VNPAY. Không được
		// trùng lặp trong ngày.
		String vnp_IpAddr = Config.getIpAddress(request);// Địa chỉ IP của khách hàng thực hiện giao dịch.

		String vnp_TmnCode = Config.vnp_TmnCode;// Mã website của merchant trên hệ thống của VNPAY.

		String vnp_TransactionNo = vnp_TxnRef;// Mã giao dịch ghi nhận tại hệ thống VNPAY.
		String vnp_hashSecret = Config.vnp_HashSecret;//

		int amount = (int) amountOrder * 100;
		Map<String, String> vnp_Params = new HashMap<>();
		vnp_Params.put("vnp_Version", vnp_Version);
		vnp_Params.put("vnp_Command", vnp_Command);
		vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
		vnp_Params.put("vnp_Amount", String.valueOf(amount));
		vnp_Params.put("vnp_CurrCode", "VND");
		String bank_code = "NCB";
		if (bank_code != null && !bank_code.isEmpty()) {
			vnp_Params.put("vnp_BankCode", bank_code);
		}
		vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
		vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
		vnp_Params.put("vnp_OrderType", orderType);

		String locate = "";
		if (locate != null && !locate.isEmpty()) {
			vnp_Params.put("vnp_Locale", locate);
		} else {
			vnp_Params.put("vnp_Locale", "vn");
		}
		vnp_Params.put("vnp_ReturnUrl", Config.vnp_Returnurl);
		vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

		Date dt = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(dt);
		String vnp_CreateDate = dateString;
		String vnp_TransDate = vnp_CreateDate;
		vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

		// Build data to hash and querystring
		List fieldNames = new ArrayList(vnp_Params.keySet());
		Collections.sort(fieldNames);
		StringBuilder hashData = new StringBuilder();
		StringBuilder query = new StringBuilder();
		Iterator itr = fieldNames.iterator();
		while (itr.hasNext()) {
			String fieldName = (String) itr.next();
			String fieldValue = (String) vnp_Params.get(fieldName);
			System.out.println("fieldName=" + fieldName);
			if ((fieldValue != null) && (fieldValue.length() > 0)) {
				// Build hash data
				hashData.append(fieldName);
				hashData.append('=');
				hashData.append(fieldValue);
				// Build query
				query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
				query.append('=');
				query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

				if (itr.hasNext()) {
					query.append('&');
					hashData.append('&');
				}
			}
		}
		String queryUrl = query.toString();
		String vnp_SecureHash = Config.Sha256(Config.vnp_HashSecret + hashData.toString());
		// System.out.println("HashData=" + hashData.toString());
		queryUrl += "&vnp_SecureHashType=SHA256&vnp_SecureHash=" + vnp_SecureHash;
		String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;
		System.out.println(paymentUrl);
		return "redirect:" + paymentUrl;
	}

	@RequestMapping("/vnpay-response")
	public String vnpayResponse(HttpServletRequest request,
			@RequestParam(value = "vnp_ResponseCode", defaultValue = "0") String responseCode,
			@RequestParam(value = "vnp_OrderInfo", defaultValue = "0") String orderInfo)
			throws UnsupportedEncodingException {
		System.out.println("vnp_ResponseCode=" + responseCode);
		System.out.println("vnp_OrderInfo=" + orderInfo);
		if ("00".equals(responseCode)) {
			orderDAO.save(Integer.valueOf(orderInfo));
		}

		return "redirect:/";
	}
}
