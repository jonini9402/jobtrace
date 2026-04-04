package com.jobtrace.repository;

import com.jobtrace.domain.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(@NotBlank String email);

    User findByEmail(@NotBlank String email);

    void deleteByEmail(String email);

}
