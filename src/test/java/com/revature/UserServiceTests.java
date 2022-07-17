package com.revature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
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
import com.revature.entities.User;
import com.revature.service.UserService;

class UserServiceTests {

	private UserService uServ;
	private UserRepository uRepo;
	private CurrencyPairRepository cpRepo;

	@BeforeEach
	void setup() {
		uRepo = mock(UserRepository.class);
		cpRepo = mock(CurrencyPairRepository.class);
		uServ = new UserService(uRepo, cpRepo);
	}

	// ------- add(User u) ------- //
	@Test
	void testAdd_success() {
		User u = new User();
		u.setId(1);

		when(uRepo.save(u)).thenReturn(u);

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
	void testGetById_returnNull() {
		User u = uServ.getById(-1);

		assertEquals(u, null);
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
}
