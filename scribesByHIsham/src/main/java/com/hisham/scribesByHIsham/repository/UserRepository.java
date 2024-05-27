package com.hisham.scribesByHIsham.repository;

import com.hisham.scribesByHIsham.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
     Optional<User> findByUsernameOrEmail(String username, String email);
     Optional<User> findById(Long userId);
     Boolean existsByUsername(String username);

     Boolean existsByEmail(String email);
}
