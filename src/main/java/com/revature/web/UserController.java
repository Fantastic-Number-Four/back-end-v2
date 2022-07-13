package com.revature.web;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.entities.User;
import com.revature.service.UserService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/users")
public class UserController {

	private UserService uServ;
	
	@Autowired
	public UserController(UserService uServ) {
		super();
		this.uServ = uServ;
	}
	
	@PostMapping("/add")
	public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
		return ResponseEntity.ok(uServ.add(user));
	}
	
	
	
	@GetMapping("/{id}")
	public ResponseEntity<Set<String>> findById(@PathVariable("id") int id){
		return ResponseEntity.ok(uServ.getById(id).getCurrencyPairs().stream().map(address -> address.getAddress()).collect(Collectors.toSet()));
	}
	
	@GetMapping("/findall")
	public ResponseEntity<Set<User>> getAll(){
		return ResponseEntity.ok(uServ.findAll());
		
	}
	
		
	
	
	
	
}
