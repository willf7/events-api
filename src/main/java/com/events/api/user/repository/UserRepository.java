package com.events.api.user.repository;

import com.events.api.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByEmail(String userName);

    boolean existsByEmail(String email);
}
