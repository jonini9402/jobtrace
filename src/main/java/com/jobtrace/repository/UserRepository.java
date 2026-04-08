package com.jobtrace.repository;

import com.jobtrace.domain.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(@NotBlank String email);

    Optional<User> findByEmail(@NotBlank String email);

    void deleteByEmail(String email);

    CharSequence findBypassword(String password);
}
