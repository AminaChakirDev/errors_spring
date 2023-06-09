package com.livecoding.blog.repository;

import com.livecoding.blog.entity.ERole;
import com.livecoding.blog.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
