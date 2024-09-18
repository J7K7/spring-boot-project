package com.bussiness.advail_capitals.services;

import com.bussiness.advail_capitals.dtos.LoginUserRequestDto;
import com.bussiness.advail_capitals.dtos.RegisterUserRequestDto;
import com.bussiness.advail_capitals.entities.Role;
import com.bussiness.advail_capitals.entities.User;
import com.bussiness.advail_capitals.repositories.RoleRepository;
import com.bussiness.advail_capitals.repositories.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.regex.Pattern;

@Service
public class AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    // Regex for validating email format
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    public User signup(RegisterUserRequestDto input) {
        try {

            logger.info("Signing up user: {}", input.getEmail());

            Role role = roleRepository.findByName(input.getUserType())
            .orElseGet(() -> {
                Role newRole = new Role();
                newRole.setName(input.getUserType());
                return roleRepository.save(newRole);
            });

            if (input.getEmail() == null || input.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("Email must not be empty");
            }
            if (input.getFullName() == null || input.getFullName().trim().isEmpty()) {
                throw new IllegalArgumentException("Full Name must not be empty");
            }
            if (!EMAIL_PATTERN.matcher(input.getEmail()).matches()) {
                throw new IllegalArgumentException("Invalid email format");
            }
            if (input.getPassword() == null || input.getPassword().trim().isEmpty()) {
                throw new IllegalArgumentException("Password must not be empty");
            }
            
            User user = new User()
                .setFullName(input.getFullName())
                .setEmail(input.getEmail())
                .setPassword(passwordEncoder.encode(input.getPassword()))
                .setRoles(Set.of(role));

            logger.info("Saving user: {}", user.getEmail());

            return userRepository.save(user);

        } catch (IllegalArgumentException e){
            logger.error("Authentication failed for user: {}", "", e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        } catch (Exception e){
            logger.error("An unexpected error occurred during register for user: {}", input.getEmail(), e);
            throw new RuntimeException("An unexpected error occurred during register !", e);
        }
    }

    public User authenticate(LoginUserRequestDto input) {
        try {
            
            logger.info("Log in user: {}", input.getEmail());
            if (input.getEmail() == null || input.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("Email must not be empty");
            }
            if (!EMAIL_PATTERN.matcher(input.getEmail()).matches()) {
                throw new IllegalArgumentException("Invalid email format");
            }
            if (input.getPassword() == null || input.getPassword().trim().isEmpty()) {
                throw new IllegalArgumentException("Password must not be empty");
            }

            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    input.getEmail(),
                    input.getPassword()
                )
            );

            return userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found !"));
        } catch (AuthenticationException e) {
            logger.error("Authentication failed for user: {}", input.getEmail(), e);
            throw new BadCredentialsException("Invalid Email or Password !");
        } catch (IllegalArgumentException e){
            logger.error("Authentication failed for user: {}", "", e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        } catch (Exception e){
            logger.error("An unexpected error occurred during authentication for user: {}", input.getEmail(), e);
            throw new RuntimeException("An unexpected error occurred during authentication !", e);
        }
    }
}
