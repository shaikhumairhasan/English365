package com.project.English365.repositories;

import org.springframework.stereotype.Repository;

import com.project.English365.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository("userRepository")
public interface UserRepository extends JpaRepository <User, Long>{

	User findByEmailIgnoreCase(String email);

	User findByEmail(String email);
	
}
