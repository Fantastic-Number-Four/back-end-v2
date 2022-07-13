package com.revature.data;

import org.springframework.data.jpa.repository.JpaRepository;


import com.revature.entities.Watchlist;

public interface WatchlistRepository extends JpaRepository<Watchlist, Integer>{
	Watchlist  findByUserId(int userId);
}
