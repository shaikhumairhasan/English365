package com.project.English365.controllers;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.project.English365.entities.User;

@Controller
public class SiteController {

	@GetMapping("/")
	public String home() {
		return "home";
	}
	
	@GetMapping("/home")
	public String backHome() {
		return "home";
	}
	
	@GetMapping("/about")
	public String about() {
		return "about";
	}
	
	@GetMapping("/login")
	public String login() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
		return "redirect:/dashboard";
	}
	
	@GetMapping(value="/register")
    public ModelAndView registration(ModelAndView modelAndView)
    {
		User userEntity = new User();
        modelAndView.addObject("userEntity", userEntity);
        modelAndView.setViewName("register");
        return modelAndView;
    }
	
	@GetMapping(value="/forgot")
    public ModelAndView forgot(ModelAndView modelAndView, User userEntity)
    {
        modelAndView.addObject("userEntity", userEntity);
        modelAndView.setViewName("forgot");
        return modelAndView;
    }
	
	@GetMapping("/verifyEmail")
	public String verifyEmail() {
		return "verifyEmail";
	}
}
