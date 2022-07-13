package com.revature.web;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.entities.Watchlist;
import com.revature.service.WatchlistService;



@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/watchlist")
public class WatchlistController {
	
	private WatchlistService wlServ;
	
	@Autowired
	public WatchlistController(WatchlistService wlServ) {
		super();
		this.wlServ = wlServ;
	}
	
	@GetMapping("/findall")
	public ResponseEntity<Set<Watchlist>> getAll(){
		return ResponseEntity.ok(wlServ.findAll());
		
		
	}
	

	
	@GetMapping("/{id}")
	public ResponseEntity<Watchlist> findWatchlistById(@PathVariable("id") int id){
		return ResponseEntity.ok(wlServ.getById(id));
	}
	
	@GetMapping("/userId/{userId}")
	public ResponseEntity<Watchlist> findWatchlistByUserId(@PathVariable("userId") int id){
		return ResponseEntity.ok(wlServ.getByUserId(id));
	}
	
	@PostMapping("/add")
	public ResponseEntity<Watchlist> addWatchlist(@RequestBody Watchlist w){
		return ResponseEntity.ok(wlServ.add(w));
	}
	
	

	
		
}
	
	

	
	
	
	
	

