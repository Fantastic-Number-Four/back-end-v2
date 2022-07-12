package com.revature.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	
	
}
