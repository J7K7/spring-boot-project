package com.bussiness.advail_capitals.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bussiness.advail_capitals.dtos.LoginResponseDto;
import com.bussiness.advail_capitals.dtos.LoginUserRequestDto;
import com.bussiness.advail_capitals.dtos.RegisterUserRequestDto;
import com.bussiness.advail_capitals.entities.User;
import com.bussiness.advail_capitals.services.AuthenticationService;
import com.bussiness.advail_capitals.services.JwtService;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/adminSignup")
    public ResponseEntity<?> adminRegister(@RequestBody RegisterUserRequestDto registerUserDto) {
        try{
            registerUserDto.setUserType("ROLE_ADMIN");

            User registeredUser = authenticationService.signup(registerUserDto);

            return ResponseEntity.ok(registeredUser);
            
        } catch (IllegalArgumentException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", exception.getMessage());
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Internal Server Error !");
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody RegisterUserRequestDto registerUserDto) {
        try{
            registerUserDto.setUserType("ROLE_USER");

            User registeredUser = authenticationService.signup(registerUserDto);

            return ResponseEntity.ok(registeredUser);

        } catch (IllegalArgumentException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", exception.getMessage());
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Internal Server Error !");
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginUserRequestDto loginUserDto) {
        try {
            User authenticatedUser = authenticationService.authenticate(loginUserDto);
            String jwtToken = jwtService.generateToken(authenticatedUser);

            LoginResponseDto loginResponse = new LoginResponseDto().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());

            return ResponseEntity.ok(loginResponse);
        } catch (AuthenticationException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", exception.getMessage());
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Internal Server Error !");
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String helloAdmin(){
        return "Hello Admin!";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user")
    public String helloUser(){
        return "Hello User!";
    }
}