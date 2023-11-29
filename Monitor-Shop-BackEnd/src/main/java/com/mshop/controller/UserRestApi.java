package com.mshop.controller;

import java.util.List;

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

import com.mshop.models.Cart;
import com.mshop.models.User;
import com.mshop.repositories.CartRepository;
import com.mshop.repositories.UserRepository;

@CrossOrigin("*")
@RestController
@RequestMapping("api/users")
public class UserRestApi {
	@Autowired
	UserRepository repo;
	
	@Autowired
	CartRepository Crepo;

//	@Autowired
//	PasswordEncoder passwordEncoder;
	
	@GetMapping
	public ResponseEntity<List<User>> getAll() {
		return ResponseEntity.ok(repo.findByStatusTrueAndRoleFalse());
	}
	
	@GetMapping("{id}")
	public ResponseEntity<User> getOne(@PathVariable("id") Long id) {
		if(!repo.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(repo.findById(id).get());
	}
	
	@GetMapping("email/{email}")
	public ResponseEntity<User> getOneByEmail(@PathVariable("email") String email) {
		return ResponseEntity.ok(repo.findByEmail(email));
	}
	
	@PostMapping
	public ResponseEntity<User> post(@RequestBody User user) {
		if(repo.existsByEmail(user.getEmail())) {
			return ResponseEntity.notFound().build();
		}
		if(repo.existsById(user.getUserId())) {
			return ResponseEntity.badRequest().build();
		}
		//user.setPassword(passwordEncoder.encode(user.getPassword()));
		User u =  repo.save(user);

		Cart c = new Cart(0L, 0.0, user.getAddress(), user.getPhone(), true, u);
		Crepo.save(c);
		return ResponseEntity.ok(u);
	}
	
	@PutMapping("{id}")
	public ResponseEntity<User> put(@PathVariable("id") Long id, @RequestBody User user) {
		if(!repo.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		if(!id.equals(user.getUserId())) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(repo.save(user));
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
		if(!repo.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		User u = repo.findById(id).get();
		u.setStatus(false);
		repo.save(u);
		return ResponseEntity.ok().build();
	}
}
