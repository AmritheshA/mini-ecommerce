package com.microservice.authservice.authservice.Service;

import com.microservice.authservice.authservice.Entity.UserEntity;
import com.microservice.authservice.authservice.Service.JWT.JwtService;
import com.microservice.authservice.authservice.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Override
    public String saveUser(UserEntity userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        userRepository.save(userInfo);
        return "User Registered Successfully";
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean isEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public ResponseEntity<?> registerUser(UserEntity userInfo) {

        if (isEmailExist(userInfo.getEmail())) {
            return new ResponseEntity<>("Email Already Exist", HttpStatus.BAD_REQUEST);
        }
        userInfo.setRole("USER");
        saveUser(userInfo);
        return new ResponseEntity<>(userInfo, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> loginUser(String email, String password) {
        if (isEmailExist(email)) {
            UserEntity user = findByEmail(email);
        } else {
            return new ResponseEntity<>("Email Not Exist", HttpStatus.BAD_REQUEST);
        }
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(email, password));
            final String jwtToken = "Bearer Token" + jwtService.generateToken(authentication, email);

            return new ResponseEntity<>(jwtToken, HttpStatus.OK);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return new ResponseEntity<>("Invalid Credential", HttpStatus.UNAUTHORIZED);
        }
    }
}
