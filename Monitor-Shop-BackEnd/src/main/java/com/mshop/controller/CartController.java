package com.mshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mshop.models.Cart;
import com.mshop.repositories.CartDetailRepository;
import com.mshop.repositories.CartRepository;
import com.mshop.repositories.UserRepository;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/cart")
public class CartController {
	@Autowired
	CartRepository repo;
	
	@Autowired
	CartDetailRepository CDrepo;
	
	@Autowired
	UserRepository Urepo;
	
	@GetMapping("/user/{id}")
	public ResponseEntity<Cart> getCartUser(@PathVariable("id") Long id) {
		if(!Urepo.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(repo.getCartUser(id));
	}
	
	@PutMapping("/user/{id}")
	public ResponseEntity<Cart> putCartUser(@PathVariable("id") Long id, @RequestBody Cart cart) {
		if(!Urepo.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(repo.save(cart));
	}
}
