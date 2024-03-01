package com.microservice.authservice.authservice.Controller;

import com.microservice.authservice.authservice.Entity.UserEntity;
import com.microservice.authservice.authservice.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final UserService userService;

    @Autowired
    private AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserEntity userInfo) {
        return userService.registerUser(userInfo);
    }

    @GetMapping("/check")
    public String sample() {
        return "Hello this is auth controller ";
    }

    @GetMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam("email") String email,
                                            @RequestParam("password") String password) {
        return userService.loginUser(email, password);
    }
}