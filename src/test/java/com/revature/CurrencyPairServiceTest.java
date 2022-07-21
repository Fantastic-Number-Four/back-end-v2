package com.revature;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.revature.data.CurrencyPairRepository;
import com.revature.entities.CurrencyPair;
import com.revature.entities.User;
import com.revature.service.CurrencyPairService;

@ExtendWith(MockitoExtension.class)
public class CurrencyPairServiceTest {

	@InjectMocks
	private CurrencyPairService cpServ;
	@Mock
	private CurrencyPairRepository cpMockdao;
	

	private User user1;
	private CurrencyPair cp1;
	private Set<User> setUser;
	private Set<CurrencyPair> setCp;
	private User user;
	private CurrencyPair cp;
	
	
    @BeforeEach
    public void setup(){
    	user1 = new User();
    	cp1 = new CurrencyPair();
    	setUser = new HashSet<User>();
    	setCp = new HashSet<CurrencyPair>();
    	setUser.add(user1);
    	setCp.add(cp1);
    	user = new User(1,"fjdks","jfksdf",setCp );
    	cp = new CurrencyPair(1,"address",setUser);
    }
    
    

    @DisplayName("JUnit test for remove method")
	@Test
	void TestRemoveCurrencyPair() {
    
    	cpServ.remove(user.getId());
    	verify(cpMockdao, times(1)).deleteById(user.getId());
   	
		
	}
}
