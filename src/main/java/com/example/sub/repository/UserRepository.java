package com.example.sub.repository;

import com.example.sub.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    User findByUid (Long uid);
}
