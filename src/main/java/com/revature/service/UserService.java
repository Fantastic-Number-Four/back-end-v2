package com.revature.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.revature.data.CurrencyPairRepository;
import com.revature.data.UserRepository;
import com.revature.entities.User;
import com.revature.exceptions.UserNotFoundException;

@Service
public class UserService {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	private UserRepository userRepo;
	private CurrencyPairRepository cpRepo;
	
	@Autowired
	public UserService(UserRepository userRepo, CurrencyPairRepository cpRepo) {
		super();
		this.userRepo = userRepo;
		this.cpRepo = cpRepo;
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public User add(User u) {
		return userRepo.save(u);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void remove(int id) {
		userRepo.deleteById(id);
	}
	
	@Transactional(readOnly=true)
	public User getById(int id) {
		if (id <= 0) {
			log.warn("Id cannot be <= 0. Id paasswed was: {}", id);
			return null;
		}
		
		return userRepo.findById(id)
				.orElseThrow(() -> new UserNotFoundException("No user found with id " + id));
	}
	
}
