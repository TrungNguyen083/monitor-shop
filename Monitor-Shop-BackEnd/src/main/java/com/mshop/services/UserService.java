package com.mshop.services;

import com.mshop.models.Cart;
import com.mshop.models.User;
import com.mshop.repositories.CartRepository;
import com.mshop.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepo;
    private final CartRepository cartRepo;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;
    @Autowired
    public UserService(UserRepository userRepo, CartRepository cartRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.cartRepo = cartRepo;
        this.passwordEncoder = passwordEncoder;
        this.mapper = new ModelMapper();
    }
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
    public User getUserById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Transactional
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepo.save(user);
        Cart c = new Cart(0L, 0.0, user.getAddress(), user.getPhone(), true, savedUser);
        cartRepo.save(c);
        return savedUser;
    }

    public User updateUser(Long id, User user) {
    	User u = getUserById(id);
        mapper.map(user, u);
        return userRepo.save(u);
    }

    public boolean checkEmail(String email) {
    	return userRepo.existsByEmail(email);
    }

    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }
}
