package com.bigdeal.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SendMailController {
	@Autowired
	private JavaMailSender mailSender;
	
	
	
	@GetMapping("/contact")
	public String showContactForm() {
		 return "SendMail";
	}
	
	@PostMapping("/send")
	public String subcontact(HttpServletRequest rq,Model model) {
		String fullname = rq.getParameter("fullname");
		String email = rq.getParameter("email");
		String subject = rq.getParameter("subject");
		String content = rq.getParameter("content");
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("contact@gmail.com");
		message.setTo(email);
		
		String mailSubject = fullname + "has sent a message";
		String mailContent = "Sender Name: " + fullname + "\n";
		mailContent += "Sender E-mail: " + email + "\n";
		mailContent += "Subject:: " + subject + "\n";
		mailContent += "Content: " + content + "\n";
		
		message.setSubject(mailSubject);
		message.setText(mailContent);
		
		mailSender.send(message);
//		model.addAttribute("mes", "Gửi thành công");
		return "SendMail";
	}
}
