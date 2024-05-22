package com.project.English365.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.project.English365.entities.User;
import com.project.English365.repositories.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder; // Inject PasswordEncoder

	@Autowired
	public CustomAuthenticationFailureHandler(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		// Retrieve the username from the request parameters
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		// Log or handle the failed authentication attempt with the username
		System.out.println("Failed login attempt for username: " + username);

		// Load user details by username
		User userDB = userRepository.findByEmail(username);
		
		if (userDB == null) {
		    response.sendRedirect("/failed?user=notfound");
		    return;
		}

		boolean isEnabled = userDB.isEnabled();
		System.out.println(passwordEncoder.encode(password) + " : " + userDB.getPassword());
		System.out.println(isEnabled);
		
		if (passwordEncoder.matches(password, userDB.getPassword())) {
			if (!isEnabled) {
				response.sendRedirect("/disable?username=" + username);
			}

		} else {
			response.sendRedirect("/failed?user=incorrect");
		}
	}
}
