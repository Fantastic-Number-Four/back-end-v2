package com.revature.web;

import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.revature.entities.CurrencyPair;
import com.revature.entities.User;
import com.revature.service.UserService;
import com.revature.util.JwtTokenManager;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/users")
public class UserController {

	private UserService uServ;
	private JwtTokenManager tokenManager;
	
	@Autowired
	public UserController(UserService uServ, JwtTokenManager tokenManager) {
		super();
		this.uServ = uServ;
		this.tokenManager = tokenManager;
	}
	
//	@PostMapping("/add")
//	public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
//		return ResponseEntity.ok(uServ.add(user));
//	}
	
	
	
	@GetMapping("/watchlist")
	public ResponseEntity<Set<String>> findWatchlistById(@RequestHeader("jwt-token") String token){
        int userId = tokenManager.parseUserIdFromToken(token);
		
		return ResponseEntity.ok(uServ.getById(userId).getCurrencyPairs().stream()
				.map(address -> address.getAddress()).collect(Collectors.toSet()));
	}
	
//	@GetMapping("/findall")
//	public ResponseEntity<Set<User>> getAll(){
//		return ResponseEntity.ok(uServ.findAll());
//		
//	}
	
	@PostMapping("/add")
	public void addToWatchlist(@RequestHeader("jwt-token") String token, @Valid @RequestBody CurrencyPair currencyPair) {        
        int userId = tokenManager.parseUserIdFromToken(token);
        
        User user = uServ.getById(userId);
        Set<CurrencyPair> watchlist = user.getCurrencyPairs();
        watchlist.add(currencyPair);
        
        user.setCurrencyPairs(watchlist);
        
        uServ.add(user);
	}
	
	@DeleteMapping("/remove")
	public void removeFromWatchlist(@RequestHeader("jwt-token") String token, @Valid @RequestBody CurrencyPair currencyPair) {        
        int userId = tokenManager.parseUserIdFromToken(token);
        
        User user = uServ.getById(userId);
        Set<CurrencyPair> watchlist = user.getCurrencyPairs();
        watchlist.remove(currencyPair);
        
        user.setCurrencyPairs(watchlist);
        
        uServ.add(user);
	}
	
}
