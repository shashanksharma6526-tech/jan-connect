package com.jan_connect.backend.repository;

import com.jan_connect.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// JpaRepository<User, Long> gives you 20+ free methods:
// save(), findById(), findAll(), deleteById(), existsById(), count(), etc.
// You don't write SQL for basic operations — Spring Data JPA handles it automatically.
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA automatically generates the SQL for this method
    // based on the method name. "findByEmail" → SELECT * FROM users WHERE email = ?
    Optional<User> findByEmail(String email);

    // Check whether a user exists with the given email. Used during registration.
    boolean existsByEmail(String email);
}
