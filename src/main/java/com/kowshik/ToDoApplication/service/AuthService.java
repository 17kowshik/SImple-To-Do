package com.kowshik.ToDoApplication.service;

import com.kowshik.ToDoApplication.exception.InvalidCredentialsException;
import com.kowshik.ToDoApplication.exception.UserAlreadyExistsException;
import com.kowshik.ToDoApplication.exception.WeakPasswordException;
import com.kowshik.ToDoApplication.model.Users;
import com.kowshik.ToDoApplication.repository.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private HttpServletRequest request;

    public String registerUser(Users user) {
        if (!isStrongPassword(user.getPassword())) {
            throw new WeakPasswordException("Password must be at least 8 characters long and contain an uppercase letter, a lowercase letter, and a special character.");
        }
        if (doesUserAlreadyExist(user.getUsername())) {
            throw new UserAlreadyExistsException("User with this username already exists.");
        }

        usersRepository.save(user);
        String ipAddress = getClientIp();
        logger.info("User '{}' registered successfully from IP {}", user.getUsername(), ipAddress);
        return "User Registered Successfully!";
    }

    public String loginUser(Users user) {
        String ipAddress = getClientIp();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

            if (!authentication.isAuthenticated()) {
                logger.warn("Failed authentication attempt for user '{}' from IP {}", user.getUsername(), ipAddress);
                throw new InvalidCredentialsException("Invalid credentials.");
            }

            logger.info("User '{}' logged in from IP {}", user.getUsername(), ipAddress);
            return jwtService.generateJWT(user.getUsername());

        } catch (Exception e) {
            logger.warn("Failed authentication attempt for user '{}' from IP {}", user.getUsername(), ipAddress);
            throw new InvalidCredentialsException("Invalid credentials.");
        }
    }

    public boolean doesUserAlreadyExist(String username) {
        return username != null && !username.isBlank() && usersRepository.findByUsername(username) != null;
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
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (specialChars.indexOf(c) != -1) hasSpecial = true;

            if (hasUpper && hasLower && hasSpecial) return true;
        }

        return false;
    }

    private String getClientIp() {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}