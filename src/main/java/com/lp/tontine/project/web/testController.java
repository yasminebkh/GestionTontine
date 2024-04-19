package com.lp.tontine.project.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lp.tontine.project.entities.User;

@Controller
@RequestMapping("/test")
public class testController {
	
	@GetMapping("/page")
    public String getLoginPage(Model model) {
		//model.addAttribute("user",new User());
        return "test";
    }

}
