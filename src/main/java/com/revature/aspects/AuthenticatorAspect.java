package com.revature.aspects;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.revature.exceptions.AuthenticationException;
import com.revature.service.UserService;
import com.revature.util.JwtTokenManager;

@Aspect
@Configuration
public class AuthenticatorAspect {

	private UserService uServ;
	private JwtTokenManager tokenManager;
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Before("execution (* com.revature.web.UserController.*(..))")
	public void authenticate(JoinPoint joinPoint) {
		log.info("Checking for user access...");
		
		try {
			RequestAttributes ra = RequestContextHolder.getRequestAttributes();
	        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
	        HttpServletRequest request = sra.getRequest();
	        
	        String token = request.getHeader("jwt-token");
	        
	        int userId = tokenManager.parseUserIdFromToken(token);
	        
	        uServ.getById(userId); // this method will throw an exception if the user cannot be found in the db
	        log.info("User with id " + userId + " granted access.");
		} catch (Exception e) {
			log.error("Unable to authenticate. Please sign in again.");
			throw new AuthenticationException("Unable to authenticate. Please sign in again.");
		}
	}
	
}
