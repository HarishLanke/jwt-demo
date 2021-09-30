package com.epam.rd.jwtdemo.controller;

import com.epam.rd.jwtdemo.config.jwt.JwtResponse;
import com.epam.rd.jwtdemo.config.jwt.JwtUtils;
import com.epam.rd.jwtdemo.model.User;
import com.epam.rd.jwtdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public User registerUser(@RequestBody User user){
        return userService.register(user);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody User user){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        return ResponseEntity.ok(new JwtResponse(user.getUsername(),
                jwtUtils.generateToken(user.getUsername())));
    }

    @GetMapping("/home")
    public String homePage(){
        return "Welcome to Secured Api";
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String,String>> handleBadCredentialsException(BadCredentialsException exception, WebRequest request){
        Map<String,String> response = new HashMap<>();
        response.put("message", "Invalid username or password.");
        response.put("timestamp",new Date().toString());
        response.put("uri",request.getDescription(false));
        response.put("status-code", HttpStatus.BAD_REQUEST.name());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
