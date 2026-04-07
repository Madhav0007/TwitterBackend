package com.integ.task.repository;

import com.integ.task.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByUsername(String username);
}
