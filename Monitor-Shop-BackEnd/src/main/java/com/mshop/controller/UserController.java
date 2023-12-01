package com.mshop.controller;

import java.util.List;

import com.mshop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mshop.models.User;

@CrossOrigin("*")
@RestController
@RequestMapping("api/users")
public class UserController {

	@Autowired
	UserService userService;
	
	@GetMapping
	public ResponseEntity<List<User>> getAll() {
		return ResponseEntity.ok(userService.getAllUsers());
	}
	
	@GetMapping("{id}")
	public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
		return ResponseEntity.ok(userService.getUserById(id));
	}
	
	@GetMapping("email/{email}")
	public ResponseEntity<User> getOneByEmail(@PathVariable("email") String email) {
		return ResponseEntity.ok(userService.getUserByEmail(email));
	}
	
	@PostMapping
	public ResponseEntity<User> post(@RequestBody User user) {
		if(userService.checkEmail(user.getEmail())) {
			return ResponseEntity.ok(userService.createUser(user));
		}
		return ResponseEntity.notFound().build();
	}
	
	@PutMapping("{id}")
	public ResponseEntity<User> put(@PathVariable("id") Long id, @RequestBody User user) {
		if(userService.checkEmail(user.getEmail())) {
			return ResponseEntity.ok(userService.updateUser(id, user));
		}
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
		try {
			userService.deleteUser(id);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
}
