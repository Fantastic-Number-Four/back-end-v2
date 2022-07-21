package com.revature;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.revature.data.CurrencyPairRepository;
import com.revature.data.UserRepository;
import com.revature.entities.CurrencyPair;
import com.revature.entities.User;
import com.revature.exceptions.UserNotFoundException;
import com.revature.service.UserService;


class UserServiceTests {

	private UserService uServ;
	private UserRepository uRepo;
	private CurrencyPairRepository cpRepo;
	private User user1;
	private CurrencyPair cp1;
	private Set<User> setUser;
	private Set<CurrencyPair> setCp;
	private User user;
	private CurrencyPair cp;
	
	@BeforeEach
	void setup() {
		uRepo = mock(UserRepository.class);
		cpRepo = mock(CurrencyPairRepository.class);
		uServ = new UserService(uRepo, cpRepo);
    	user1 = new User();
    	cp1 = new CurrencyPair();
    	setUser = new HashSet<User>();
    	setCp = new HashSet<CurrencyPair>();
    	setUser.add(user1);
    	setCp.add(cp1);
    	user = new User(1,"fjdks","jfksdf",setCp );
    	cp = new CurrencyPair(1,"address",setUser);
	}
	
	// ------- add(User u) ------- //
	@Test
	void testAdd_success() {
		
		User u = user;
		u.setId(1);

		when(uRepo.save(u)).thenReturn(u);
		if (u.getCurrencyPairs() != null) {
		u.getCurrencyPairs().forEach(currencyPairs -> cpRepo.save(currencyPairs));
		   }
		User u2 = uServ.add(u);
		
		assertEquals(u, u2);
	}

	// ------- getById(int id) ------- //
	@Test
	void testGetById_success() {
		Optional<User> u = Optional.ofNullable(new User());

		when(uRepo.findById(1)).thenReturn(u);

		Optional<User> u2 = Optional.ofNullable(uServ.getById(1));

		assertEquals(u, u2);
	}
	
	@Test
	void testGetById_Fail() {
	
        Exception exception = assertThrows(UserNotFoundException.class, () -> uServ.getById(1));

        String expectedMessage = "No user found with id 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
	
	
		
	}

	@Test
	void testGetById_returnNull() {
		User u = uServ.getById(-1);

		assertEquals(null, u);
	}

	// ------- findByPublicAddress(String publicAddress) ------- //
	@Test
	void testFindByPublicAddress() {
		String address = "0x8797ABc4641dE76342b8acE9C63e3301DC35e3d8";
		User u = new User();
		u.setPublicAddress(address);

		when(uRepo.findUserByPublicAddress(address)).thenReturn(u);

		User u2 = uServ.findByPublicAddress(address);

		assertEquals(u, u2);
	}

	// ------- findAll() ------- //
	@Test
	void testFindAll() {
		User u = new User();
		User u2 = new User();
		u.setId(1);
		u2.setId(2);

		List<User> list = new ArrayList<User>();
		list.add(u);
		list.add(u2);

		when(uRepo.findAll()).thenReturn(list);

		Set<User> expected = new HashSet<User>();
		expected.add(u);
		expected.add(u2);

		Set<User> actual = uServ.findAll();

		assertEquals(expected, actual);
	}
	
	@Test 
	void removeById() {
		uServ.remove(1);
		verify(uRepo, times(1)).deleteById(1);
		
	}
}
