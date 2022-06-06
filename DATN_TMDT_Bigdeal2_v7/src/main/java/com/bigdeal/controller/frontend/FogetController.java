package com.bigdeal.controller.frontend;

import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bigdeal.dao.AccountDAO;
import com.bigdeal.dao.ProductDAO;
import com.bigdeal.entity.Account;



@Controller
@Transactional
public class FogetController {

	public String RANDOM = getRandomNumberString();
	public String RANDOM1;
	
	public long LIMITITME = 1 * 60;
	
	public long start;

	public long end;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private AccountDAO accountDAO;
	
//	form Nhập email và Username
	@RequestMapping("/forget-pwd")
	public String formfoget() {
		
		return "frontend/forget-password/forget-pwd";
	}
	

	//hàm gửi mã qua mail user hay admin đầu tiên lấy 2 giá trị từ text vào là username và email
	// mở gửi mail lên gửi theo định dang
	//lấy thời gian hệ thống về
	// check username có phải là  có tồn tại không hay email có đúng với user đăng kí không
	// mở session cho username và email
	@PostMapping("send_random_mail")
	public String layMaXN(HttpServletRequest rq, Model model, HttpSession session) {
		
		start = System.currentTimeMillis();
		System.out.println("thời gian bắt đầu :" + start);

		String username = rq.getParameter("username");
		String email = rq.getParameter("email");

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("bigdeal@gmail.com");
		message.setSentDate(new Date());
		message.setTo(email);

		//message mail user
		String mailSubject = username + " Xác thực";
		String mailContent = "Sender Name: " + username + "\n";
		mailContent += "Sender E-mail: " + email + "\n";
		mailContent += "Mã random: " + RANDOM + "\n";

		message.setSubject(mailSubject);
		message.setText(mailContent);

		if(check_email_username(username, email,model) == false) {
			model.addAttribute("mess_mail", "username không tại hoặc email không phải là email đăng ký");
			return "frontend/forget-password/forget-pwd";
		}else {
		mailSender.send(message);
		}
		model.addAttribute("mes", "Gửi thành công");

//		set session cho username và email
		session.setAttribute("username", username);
		session.setAttribute("email", email);
		
		return "redirect:/entercode";
	}
	
	
	
	
//form nhập mã random email
	@RequestMapping("/entercode")
	public String formEntercode() {
		
		return "frontend/forget-password/Entercode";
	}
	
	
	//lấy usernmae và email về
	//  nhận end : là nhận thời điểm bấm nút của hệ thống để check đã qua 1 phút chưa(LIMITTIME)
	//check random đã gửi với  đã đúng với text 
	// nếu lỗi sẽ xử lý và gửi lỗi
	// hoặc đúng sẽ điều hướng sang đỗi pass
	@PostMapping("/xacnhan_random")
	public String sendxn(HttpServletRequest rq, Model model,
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "email", required = false) String email, HttpSession session) {

		username = (String) session.getAttribute("username");
		email = (String) session.getAttribute("email");
		System.out.println("username : " + username);
		System.out.println("email : " + email);
		end = System.currentTimeMillis();

		System.out.println("thời gian kết thúc :" + end);
		String random = rq.getParameter("random");

		System.out.println("1 phút" + LIMITITME);

		System.out.println(end - start);
		if ((random.equalsIgnoreCase(RANDOM) && LIMITITME >= ((end - start) / 1000))
				|| (random.equalsIgnoreCase(RANDOM1) && LIMITITME >= ((end - start) / 1000))) {
			System.out.println("sohw random: " + RANDOM);
			System.out.println("thành công");
			session.removeAttribute("email");
			return "redirect:/reset-pw";
		} else {
			System.out.println("thất bại rồi");
			System.out.println("sohw: " + RANDOM);
			System.out.println("mã đã quá 1 phút");
			if(LIMITITME >= ((end - start) / 1000)){
				model.addAttribute("mess", "mã đã quá 1 phút mời gửi lại mã");
			}
			else {
				model.addAttribute("mess", "Sai mã mời nhập lại");
			}
			System.out.println(username);

			return "frontend/forget-password/Entercode";
		}
	}

	
	//gửi lại mật khẩu 
	// nhận thêm 1 thời gian hệ thống
	//gửi lại mã random
	//và tự động gửi usernmae: password theo đã lưu
	@RequestMapping("/guilai_Random")
	public String guilaiXN(HttpServletRequest rq, Model model,
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "email", required = false) String email, HttpSession session) {
//		boolean flag = true;

		start = System.currentTimeMillis();
		System.out.println("thời gian bắt đầu :" + start);

		Random rnd = new Random();
		RANDOM1 = String.format("%06d", rnd.nextInt(999999));

		username = (String) session.getAttribute("username");
		email = (String) session.getAttribute("email");

		session.setAttribute("username", username);
//		String random = getRandomNumberString();

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("contact@gmail.com");
		message.setTo(email);

		String mailSubject = username + "Xác thực";
		String mailContent = "Sender Name: " + username + "\n";
		mailContent += "Sender E-mail: " + email + "\n";
		mailContent += "Mã random: " + RANDOM1 + "\n";

		message.setSubject(mailSubject);
		message.setText(mailContent);

		mailSender.send(message);
		model.addAttribute("mes", "Gửi thành công");
		// thread chạy

		session.setAttribute("username", username);
		session.setAttribute("email", email);
		return "redirect:/entercode";
	}
	
	//form đỗi lại mật khẩu
	@RequestMapping("/reset-pw")
	public String formresetp() {
		
		return "frontend/forget-password/reset-pw";
	}

	
	//đỗi mật khẩu và xóa session username
	@PostMapping("Change-password")
	public String changepass(@RequestParam(value = "username", required = false) String username,HttpServletRequest rq,HttpSession session,
			@RequestParam(value = "email", required = false) String email) {
		
		
		
		username = (String) session.getAttribute("username");
		email = (String) session.getAttribute("email");
		
		System.out.println("usename là: "+username);
		System.out.println("email là: "+email);
				
		String password = rq.getParameter("password");
		String confirm = rq.getParameter("confirm");
		
		
		if(password.equals(confirm)) {
		accountDAO.Update(username,password);
		session.removeAttribute("username");
		
			return "redirect:/usr/login";
		}else {
			System.out.println("không giống nhau");
			return "frontend/forget-password/reset-pw";
		}
		
	}
	
	public static String getRandomNumberString() {
		// It will generate 6 digit random Number.
		// from 0 to 999999
		Random rnd = new Random();
		int number = rnd.nextInt(999999);

		// this will convert any number sequence into 6 character.
		return String.format("%06d", number);
	}

	//check email và user xem username đã tồn tại và có trùng với email đăng ký không
	public boolean check_email_username(String username ,String email,Model model)
	{
		boolean flag;
		Account account = accountDAO.findAccount(username);
		
		try {
		if(account.getEmail().equals(email)) {
			flag = true;
		}else {
			flag = false;
		}
		}catch (Exception e) {
			return flag = false;
		}
		
		return flag;
		
	}
}
