package com.kowshik.ToDoApplication.service;

import com.kowshik.ToDoApplication.exception.InvalidCredentialsException;
import com.kowshik.ToDoApplication.exception.UserAlreadyExistsException;
import com.kowshik.ToDoApplication.exception.WeakPasswordException;
import com.kowshik.ToDoApplication.model.Users;
import com.kowshik.ToDoApplication.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    public String registerUser(Users user) {
        if (!isStrongPassword(user.getPassword())){
            throw new WeakPasswordException("Password must be at least 8 characters long and contain an uppercase letter, a lowercase letter, and a special character.");
        } if (doesUserAlreadyExist(user.getUsername())){
            throw new UserAlreadyExistsException("User with this username already exists.");
        }
        usersRepository.save(user);
        return "User Registered Successfully!";
    }

    public String loginUser(Users user) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (!authentication.isAuthenticated()){
            throw new InvalidCredentialsException("Invalid credentials.");
        }
        return jwtService.generateJWT(user.getUsername());
    }

    public boolean doesUserAlreadyExist(String username) {
        if (username == null || username.isBlank()) {
            return false;
        }
        return usersRepository.findByUsername(username) != null;
    }

    public boolean isStrongPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasSpecial = false;
        String specialChars = "@$!%*?&";

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (specialChars.indexOf(c) != -1) {
                hasSpecial = true;
            }

            if (hasUpper && hasLower && hasSpecial) {
                return true;
            }
        }

        return false;
    }
}
