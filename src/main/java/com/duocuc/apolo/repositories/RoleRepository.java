package com.duocuc.apolo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.duocuc.apolo.models.Role;
import com.duocuc.apolo.models.User;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByName(String name);  
    Optional<Role> findByName(String name);
}