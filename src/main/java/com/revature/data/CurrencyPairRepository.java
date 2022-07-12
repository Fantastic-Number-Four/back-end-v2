package com.revature.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.entities.CurrencyPair;


@Repository
public interface CurrencyPairRepository extends JpaRepository<CurrencyPair, Integer> {

	
	
}
