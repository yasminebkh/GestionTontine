package com.lp.tontine.project.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;



import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class LogOutController {
	
	@GetMapping("/logout")
	 public String logout(Model model,HttpServletRequest request) {
		 HttpSession session = request.getSession(false);
	        if (session != null) {
	            session.invalidate(); // Invalidation de la session
	        }
		 return "redirect:/users/login";
	 }

}
