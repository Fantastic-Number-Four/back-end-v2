package com.revature.util;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.revature.entities.User;
import com.revature.exceptions.AuthenticationException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenManager {
	
	private final Key key; // from java.security
	private final Logger logger = LoggerFactory.getLogger(JwtTokenManager.class);

	public JwtTokenManager(){
		// what is a key?
		// a set of public keys used to verify a token and have it be parsed by our server
		key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	}

	// this builds the payload which is encrypted info about the user we're authenticating
	public String issueToken(User user){
		return Jwts.builder() // io.jsonwebtoken
				// payload 
				.setId(String.valueOf(user.getId()))
				.setSubject(user.getPublicAddress())
				.setIssuer("Fantastic Forex API") // the source that generated the token
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.signWith(key).compact();
	}
	
	public int parseUserIdFromToken(String token){
		
		try {
			String[] chunks = token.split("\\.");
			
			Base64.Decoder decoder = Base64.getUrlDecoder();
			
			String payload = new String(decoder.decode(chunks[1]));
			
			String[] payloadSplit = payload.split("\"");
			
			return Integer.parseInt(payloadSplit[3]);
			
		} catch (Exception e){
			logger.warn("JWT error parsing user id from token");
			throw new AuthenticationException("Unable to parse user id from JWT. Please sign in again");
		}
	}
	
}