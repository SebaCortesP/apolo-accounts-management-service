package com.duocuc.apolo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duocuc.apolo.models.Role;
import com.duocuc.apolo.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
    Optional<Role> findByName(String name);
}
