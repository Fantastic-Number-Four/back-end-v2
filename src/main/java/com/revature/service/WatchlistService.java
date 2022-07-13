package com.revature.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.revature.data.CurrencyPairRepository;
import com.revature.data.UserRepository;
import com.revature.data.WatchlistRepository;
import com.revature.entities.Watchlist;
import com.revature.exceptions.UserNotFoundException;

@Service
public class WatchlistService {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	private UserRepository userRepo;
	private CurrencyPairRepository cpRepo;
	private WatchlistRepository wlRepo;
	
	@Autowired
	public WatchlistService(UserRepository userRepo, CurrencyPairRepository cpRepo, WatchlistRepository wlRepo) {
		super();
		this.userRepo = userRepo;
		this.cpRepo = cpRepo;
		this.wlRepo = wlRepo;
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public Watchlist add(Watchlist w) {
		
		userRepo.findById(w.getUserId()).orElseThrow(() -> new UserNotFoundException("No user found with id " + w.getUserId()));
		if (w.getWatchlist() != null) {
			w.getWatchlist().forEach(watchlist -> cpRepo.save(watchlist));
		}
		return wlRepo.save(w);
		
	}
	
	@Transactional(readOnly = true)
	public Set<Watchlist> findAll(){
		
		return wlRepo.findAll().stream().collect(Collectors.toSet());
		
		
	}
	@Transactional(propagation=Propagation.REQUIRED) // default setting of transactions in Spring
	public void remove(int id) {
		
		wlRepo.deleteById(id);
	}
	

	@Transactional(readOnly=true)
	public Watchlist getById(int id) {
		
		if (id <= 0) {
			log.warn("Id cannot be <= 0. Id passed was: {}", id);
			return null;
		} else {
			return wlRepo.findById(id)
					.orElseThrow(() -> new UserNotFoundException("No user found with id " + id));
		}
		
	}
	@Transactional(readOnly=true)
	public Watchlist getByUserId(int id) {
		
		if (id <= 0) {
			log.warn("Id cannot be <= 0. Id passed was: {}", id);
			return null;
		} else {
			return wlRepo.findByUserId(id);
					
		}
		
	}
	
	

}
