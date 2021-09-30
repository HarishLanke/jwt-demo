package com.epam.rd.jwtdemo.controller;

import com.epam.rd.jwtdemo.config.jwt.JwtResponse;
import com.epam.rd.jwtdemo.model.User;
import com.epam.rd.jwtdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public User registerUser(@RequestBody User user){
        return userService.register(user);
    }
    @PostMapping("/authenticate")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody User user) {
        return userService.authenticate(user);
    }
    @GetMapping("/home")
    public String homePage(){
        return "Welcome to Secured Api";
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String,String>> handleBadCredentialsException(BadCredentialsException exception, WebRequest request){
        Map<String,String> response = new HashMap<>();
        response.put("message", exception.getMessage());
        response.put("timestamp",new Date().toString());
        response.put("uri",request.getDescription(false));
        response.put("status-code", HttpStatus.BAD_REQUEST.name());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
