package com.example.sweater.controller;

import com.example.sweater.domain.Role;
import com.example.sweater.domain.User;
import com.example.sweater.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
	@Autowired
	private UserRepo userRepo;
	
	@GetMapping("/registration")
	public String registriation() {
		
		return "registration";
	}
	
	@PostMapping("/registration")
	public String addUser(User user,  Map<String, Object> model) {
		User userFromDB = userRepo.findByUsername(user.getUsername());
		
		if(userFromDB != null){
			model.put("message", "User already exist!");
			return "registration";
		}
		
		user.setActive(true);
		user.setRoles(Collections.singleton(Role.USER));
		userRepo.save(user);
		
		return "redirect::/login";
	}
}
