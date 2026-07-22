package com.ems.repository;

import com.ems.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
	
	Optional<AppUser> findByUsername(String username);

	boolean existsByUsername(String username);
}