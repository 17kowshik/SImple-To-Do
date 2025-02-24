package com.kowshik.ToDoApplication.controller;

import com.kowshik.ToDoApplication.model.Users;
import com.kowshik.ToDoApplication.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Users user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return new ResponseEntity<>(authService.registerUser(user), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody Users user){
        return new ResponseEntity<>(authService.loginUser(user), HttpStatus.OK);
    }
}
