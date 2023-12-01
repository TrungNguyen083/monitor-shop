package com.mshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mshop.models.User;
import com.mshop.models.Login;
import com.mshop.repositories.UserRepository;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class LoginController {
	@Autowired
	UserRepository repo;
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@RequestMapping("admin/login")
	public ResponseEntity<User> LoginAdmin(@RequestBody Login login) {
		User u = new User();
		boolean checkUsername = false;
		List<User> listU = repo.findAllAdmin();
		for(User user: listU){
			if(login.getUsername().equals(user.getEmail())) {
				checkUsername = true;
				u = repo.findByEmail(login.getUsername());
				if(u.getPassword().equals(login.getPassword())) {
					return ResponseEntity.ok(u);
				}
			}
		}
		if(!checkUsername) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.badRequest().build();
	}
	
	@RequestMapping("user/login")
	public ResponseEntity<User> LoginUser(@RequestBody Login login) {
		User u;
		boolean checkUsername = false;
		List<User> listU = repo.findAllUser();
		for(User user: listU){
			if(login.getUsername().equals(user.getEmail())) {
				checkUsername = true;
				u = repo.findByEmail(login.getUsername());
				if(passwordEncoder.matches(login.getPassword(), u.getPassword())) {
					return ResponseEntity.ok(u);
				}
			}
		}
		if(!checkUsername) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.badRequest().build();
	}
}
