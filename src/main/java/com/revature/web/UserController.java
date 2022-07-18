package com.revature.web;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.entities.CurrencyPair;
import com.revature.entities.User;
import com.revature.service.CurrencyPairService;
import com.revature.service.UserService;
import com.revature.util.JwtTokenManager;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/users")
public class UserController {

	private UserService uServ;
	private CurrencyPairService cpServ;
	private JwtTokenManager tokenManager;
	
	@Autowired
	public UserController(UserService uServ, CurrencyPairService cpServ, JwtTokenManager tokenManager) {
		super();
		this.uServ = uServ;
		this.cpServ = cpServ;
		this.tokenManager = tokenManager;
	}	
	
	@GetMapping("/watchlist")
	public ResponseEntity<Set<CurrencyPair>> findWatchlistById(@RequestHeader("jwt-token") String token){
        int userId = tokenManager.parseUserIdFromToken(token);
        
        return ResponseEntity.ok(uServ.getById(userId).getCurrencyPairs());
	}
	
	@PostMapping("/add")
	public void addToWatchlist(@RequestHeader("jwt-token") String token, @Valid @RequestBody Set<CurrencyPair> currencyPairs) {        
        int userId = tokenManager.parseUserIdFromToken(token);
        
        User user = uServ.getById(userId);
        Set<CurrencyPair> watchlist = user.getCurrencyPairs();
        
        for (Iterator<CurrencyPair> it = currencyPairs.iterator(); it.hasNext(); ) {
        	CurrencyPair currencyPair = it.next();
        	watchlist.add(currencyPair);
        }
        
        user.setCurrencyPairs(watchlist);
        
        uServ.add(user);
	}
	
	@PostMapping("/replace")
	public void replaceWatchlist(@RequestHeader("jwt-token") String token, @Valid @RequestBody Set<CurrencyPair> currencyPairs) {
		int userId = tokenManager.parseUserIdFromToken(token);
		
		User user = uServ.getById(userId);
		Set<CurrencyPair> watchlist = user.getCurrencyPairs();
		
		// utility list to remove added currencyPairs in a batch
		Set<CurrencyPair> toRemove = new HashSet<>();
		
		// iterate over the watchlist and remove addresses that don't exist in the passed list
		for (Iterator<CurrencyPair> watchlistIt = watchlist.iterator(); watchlistIt.hasNext(); ) {
			CurrencyPair watchlistCP = watchlistIt.next();
			
			boolean delete = true;
			for (Iterator<CurrencyPair> currencyPairsIt = currencyPairs.iterator(); currencyPairsIt.hasNext(); ) {
				CurrencyPair passedCP = currencyPairsIt.next();
				
				if (watchlistCP.getAddress().equals(passedCP.getAddress())) {
					delete = false;
					break;
				}
			}
			
			if (delete) {
				watchlist.remove(watchlistCP);
				toRemove.add(watchlistCP);
			}
		}
		
		for (Iterator<CurrencyPair> removeIt = toRemove.iterator(); removeIt.hasNext(); ) {
			CurrencyPair remove = removeIt.next();
			cpServ.remove(remove.getId());
		}
		
		// iterate over the passed list and add addresses that don't exist in the watchlist
		for (Iterator<CurrencyPair> currencyPairsIt = currencyPairs.iterator(); currencyPairsIt.hasNext(); ) {
			CurrencyPair passedCP = currencyPairsIt.next();
			
			boolean add = true;
			for (Iterator<CurrencyPair> watchlistIt = watchlist.iterator(); watchlistIt.hasNext(); ) {
				CurrencyPair watchlistCP = watchlistIt.next();
				
				if (passedCP.getAddress().equals(watchlistCP.getAddress())) {
					add = false;
					break;
				}
			}
			
			if (add) {
				watchlist.add(passedCP);
			}
		}
		
		user.setCurrencyPairs(watchlist);
		uServ.add(user);
	}
	
	@DeleteMapping("/remove")
	public void removeFromWatchlist(@RequestHeader("jwt-token") String token, @Valid @RequestBody Set<CurrencyPair> currencyPairs) {        
        int userId = tokenManager.parseUserIdFromToken(token);
        
        User user = uServ.getById(userId);
        Set<CurrencyPair> watchlist = user.getCurrencyPairs();
        
        for (Iterator<CurrencyPair> it = currencyPairs.iterator(); it.hasNext(); ) {
        	CurrencyPair currencyPair = it.next();
        	
        	for (Iterator<CurrencyPair> it2 = watchlist.iterator(); it2.hasNext(); ) {
            	CurrencyPair current = it2.next();
            	if (currencyPair.getAddress().equals(current.getAddress())) {
            		currencyPair = current;
            		break;
            	}
            }
        	
        	watchlist.remove(currencyPair);
        	
        	cpServ.remove(currencyPair.getId());
        }
        
        user.setCurrencyPairs(watchlist);
        
        uServ.add(user);
	}
	
}
