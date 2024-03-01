package com.microservice.authservice.authservice.Service;

import com.microservice.authservice.authservice.Entity.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public String saveUser(UserEntity userInfo);

    public UserEntity findByEmail(String email);

    public boolean isEmailExist(String email);

    ResponseEntity<?> registerUser(UserEntity userInfo);

    ResponseEntity<String> loginUser(String email,String password);

}
