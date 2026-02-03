package com.lifeledger.userdetails;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserDetails, Long> {

    Optional<UserDetails> findByEmail(String email);

    Optional<UserDetails> findByName(String name);

    // ✅ REQUIRED for uniqueness check
    boolean existsByEmail(String email);
}
