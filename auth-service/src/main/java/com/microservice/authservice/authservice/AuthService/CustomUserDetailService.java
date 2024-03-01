package com.microservice.authservice.authservice.AuthService;

import com.microservice.authservice.authservice.Entity.UserEntity;
import com.microservice.authservice.authservice.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        if (!userRepo.existsByEmail(email))
            throw new UsernameNotFoundException("Username not found");
        UserEntity user = userRepo.findByEmail(email);
        return new CustomUserDetails(user);
    }
}
