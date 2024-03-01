package com.microservice.authservice.authservice.Repository;

import com.microservice.authservice.authservice.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    public UserEntity findByEmail(String email);

    public boolean existsByEmail(String email);

}
