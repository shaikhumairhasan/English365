package com.project.English365.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.project.English365.entities.User;
import com.project.English365.repositories.UserRepository;
import com.project.English365.security.OTPGenerator;
import com.project.English365.services.EmailService;

@Controller
public class UserAccountController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private EmailService emailService; 
	
	@Autowired
	private OTPGenerator otpGenerator;

	
	@PostMapping(value = "/register")
	public ModelAndView registerUser(ModelAndView modelAndView, User userEntity) {
		User existingUser = userRepository.findByEmailIgnoreCase(userEntity.getEmail());
		if (existingUser != null) {
			modelAndView.addObject("message", "Email already exists!");
			modelAndView.setViewName("login");
		} else {
			String OTP = otpGenerator.generateOTP();
			userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
			userEntity.setOtp(OTP);
			userRepository.save(userEntity);
			emailService.sendEmail(userEntity.getEmail(), "ENGLISH365 OTP Verification", "Your verification OTP is: " + OTP);
			modelAndView.addObject("messageGreen", "An OTP has been sent to your email for verification.");
			modelAndView.addObject("email", userEntity.getEmail());
			modelAndView.setViewName("verifyEmail");
		}
		return modelAndView;
	}
	

	@PostMapping(value = "/forgot")
	public ModelAndView checkNumAndEmail(ModelAndView modelAndView, User userEntity) {
		User userFromDatabase = userRepository.findByEmail(userEntity.getEmail());
		if (userFromDatabase == null) {
			modelAndView.addObject("message", "The provided email is not registered.");
			modelAndView.setViewName("forgot");
			return modelAndView;
		}
		String OTP = otpGenerator.generateOTP();
        userFromDatabase.setOtp(OTP);
        userRepository.save(userFromDatabase);
        emailService.sendEmail(userFromDatabase.getEmail(), "ENGLISH365 Password reset", "Your OTP to reset your password is: " + OTP);
        modelAndView.addObject("messageGreen", "An OTP has been sent to your email for resetting your password.");
        modelAndView.addObject("email", userFromDatabase.getEmail());
		modelAndView.setViewName("reset");
		return modelAndView;
	}
	
	
	@PostMapping("/reset")
    public ModelAndView resetPassword(
        @RequestParam String otpN,
        @RequestParam String newPassword,
        @RequestParam String confirmPassword,
        @RequestParam String email) {
		
		String otp = String.valueOf(otpN);
		ModelAndView modelAndView = new ModelAndView();
        User user = userRepository.findByEmail(email);
        if (!newPassword.equals(confirmPassword) && !otp.equals(user.getOtp())) {
        	modelAndView.addObject("message", "Invalid OTP & Passwords do not match.");
            modelAndView.addObject("email", user.getEmail());
        	modelAndView.setViewName("reset");
            return modelAndView;
        }
        if (!otp.equals(user.getOtp())) {
        	modelAndView.addObject("message", "Invalid OTP! Please try again.");
            modelAndView.addObject("email", user.getEmail());
        	modelAndView.setViewName("reset");
            return modelAndView;
        }
        if (!newPassword.equals(confirmPassword)) {
        	modelAndView.addObject("message", "Passwords do not match. Please try again.");
            modelAndView.addObject("email", user.getEmail());
        	modelAndView.setViewName("reset");
            return modelAndView;
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        modelAndView.addObject("messageGreen", "Password changed successfully!");
    	modelAndView.setViewName("login");
        return modelAndView;
    }	
	
	
	@PostMapping("/verify")
	public ModelAndView verify(@RequestParam("email") String email, @RequestParam("otpN") int otpN) {
		String otp = String.valueOf(otpN);
        ModelAndView modelAndView = new ModelAndView();
		User user = userRepository.findByEmail(email);
        if(email.isEmpty()) {
        	modelAndView.addObject("message", "Please Register First.");
			modelAndView.setViewName("verifyEmail");
        	return modelAndView;
        }
        if(otp.equals(user.getOtp())) {
        	user.setEnabled(true);
        	userRepository.save(user);
			modelAndView.addObject("messageGreen", "User Registered Successfully!");
			modelAndView.setViewName("login");
        } else {
        	modelAndView.addObject("email", email);
        	modelAndView.addObject("message", "OTP is invalid.");
            modelAndView.addObject("email", user.getEmail());
			modelAndView.setViewName("verifyEmail");
        }
		return modelAndView;
	}
	
	
	@GetMapping("/disable")
	public ModelAndView disabledAccountPage(HttpServletRequest request) {
	    ModelAndView modelAndView = new ModelAndView("verifyEmail");
	    String userEmail = request.getParameter("username");
	    if (userEmail != null) {
	        User existingUser = userRepository.findByEmailIgnoreCase(userEmail);
	        String OTP = otpGenerator.generateOTP();
	        existingUser.setOtp(OTP);
	        userRepository.save(existingUser);
	        emailService.sendEmail(existingUser.getEmail(), "ENGLISH365 OTP Verification", "Your verification OTP is: " + OTP);
	        modelAndView.addObject("message", "An OTP has been sent to your email for verification. Please verify before login.");
	        modelAndView.addObject("email", userEmail);
	    }
	    return modelAndView;
	}

   
    @GetMapping("/failed")
    public ModelAndView failedLogin(HttpServletRequest request) {
	    String errorMessage = request.getParameter("user");
    	ModelAndView modelAndView = new ModelAndView();
	    if(errorMessage.equals("notfound")) {
	    	modelAndView.addObject("message", "User not registered. Please register before login.");
	    } else {
	    	modelAndView.addObject("message", "Invalid email or password.");
	    }
    	modelAndView.setViewName("login");
    	return modelAndView;
    }

}