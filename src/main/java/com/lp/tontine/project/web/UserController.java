package com.lp.tontine.project.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lp.tontine.project.entities.Admin;
import com.lp.tontine.project.entities.Participant;
import com.lp.tontine.project.entities.User;
import com.lp.tontine.project.metier.AdminMetier;
import com.lp.tontine.project.metier.ParticipantMetier;
import com.lp.tontine.project.metier.UserMetier;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;



@Controller
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserMetier userMetier;
	@Autowired
	private ParticipantMetier participantMetier;
	@Autowired
	private AdminMetier adminMetier;
	
	
	
	@GetMapping("/login")
    public String getLoginPage(Model model) {
		model.addAttribute("user",new User());
        return "login";
    }
	
	@PostMapping("/login")
	 public String postLoginPage(@ModelAttribute("user") User user ,BindingResult resultError ,Model model ,HttpServletRequest request) {
		if(user != null ) {
			String email = user.getEmail();
	        String password = user.getPassword();
	        System.out.println("email : " + email);
	        User getUser = userMetier.getUser(email, password);
	        System.out.println("getUser : " + getUser);
	        if(resultError.hasErrors()==true) {
				for (ObjectError error : resultError.getAllErrors()) {
			        System.out.println("Erreur: " + error.getDefaultMessage());
			    }
				request.getSession().setAttribute("error", true);
				return "redirect:/users/login" ;
			}
	        if (getUser != null /*&& user.getPassword().equals(password)*/) {
				String role = getUser.getRole();
				System.out.println(role);
		        switch (role) {
		        	case "ROLE_ADMIN":
		        		Admin admin = adminMetier.getAdminDetails(user.getEmail());
		            	request.getSession().setAttribute("loggedInAdmin", admin);
		        		return "redirect:/dart/admin/home";
		            case "ROLE_PARTICIPANT":
		            	Participant participant = participantMetier.getParticipantDetails(user.getEmail());
		            	request.getSession().setAttribute("loggedInParticipant", participant);
		                return "redirect:/dart/participant/home";
		            default:
		            	request.getSession().setAttribute("loggedInUser", getUser);
		                return "redirect:/dart/user/home";
		            }
		     } else {
		    	 System.out.printf("User doesn't exist !!");
		    	 model.addAttribute("error", "Identifiants incorrects");
		         return "login";
		     }
		}else {
	        return "login";
	    }
    }
	
	@GetMapping("/signUp")
    public String getSignUpPage(Model model) {
		model.addAttribute("userSignUp", new User());
		//model.addAttribute("resultError", new BindingResult());
        return "signUp";
    }
	@PostMapping("/signUp")
	 public String postSignUpPage(@Valid @ModelAttribute("userSignUp") User user,
             BindingResult resultError,HttpServletRequest request, Model model) {
		
		User userExist = userMetier.getUserByEmail(user.getEmail());
		if(userExist != null && userExist.getEmail() != null && !userExist.getEmail().isEmpty()) {
			resultError.rejectValue("email",null,"There is already an account registered with the same email");//il enregistre une erreur au champ specifique "email" avec le message d'erreur
		}
		if(resultError.hasErrors()==true) {
			for (ObjectError error : resultError.getAllErrors()) {
		        System.out.println("Erreur: " + error.getDefaultMessage());
		    }
			request.getSession().setAttribute("error", true);
			model.addAttribute("userSignUp", user);
			return "signUp";//"redirect:/users/signUp" ;
		}
		userMetier.signUpUser(user);
        return "redirect:/users/signUp?success";
    }
	
	
    // handler method to handle list of users
//    @GetMapping("/users")
//    public String users(Model model){
//        List<User> users = userMetier.findAllUsers();
//        model.addAttribute("users", users);
//        return "users";
//    }
}
