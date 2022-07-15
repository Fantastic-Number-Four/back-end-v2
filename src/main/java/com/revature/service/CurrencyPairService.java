package com.revature.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.revature.data.CurrencyPairRepository;

@Service
public class CurrencyPairService {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	private CurrencyPairRepository cpRepo;
	
	@Autowired
	public CurrencyPairService(CurrencyPairRepository cpRepo) {
		super();
		this.cpRepo = cpRepo;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void remove(int id) {
		cpRepo.deleteById(id);
	}
	
}
