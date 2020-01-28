package com.example.sweater.service;

import com.example.sweater.domain.Role;
import com.example.sweater.domain.User;
import com.example.sweater.repos.UserRepo;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
class UserServiceTest {
	
	@Autowired
	private UserService userService;
	
	@MockBean
	private UserRepo userRepo;
	
	@MockBean
	MailSender mailSender;
	
	@MockBean
	private PasswordEncoder passwordEncoder;
	
	@Test
	void addUserTest() {
		User user = new User();
		
		user.setEmail("someTest@gem.ru");
		boolean isUserCreated = userService.addUser(user);
		
		Assert.assertTrue(isUserCreated);
		Assert.assertNotNull(user.getActivationCode());
		Assert.assertTrue(user.isActive());
		Assert.assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));
		
		Mockito.verify(userRepo, Mockito.times(1)).save(user);
		Mockito.verify(mailSender, Mockito.times(1))
				.send(
						ArgumentMatchers.eq(user.getEmail()),
						ArgumentMatchers.anyString(),
						ArgumentMatchers.contains("Welcome to Sweater")
				);
	}
	
	@Test
	void addUserFailTest() {
		User user = new User();
		
		user.setUsername("Jojo");
		
		Mockito.doReturn(new User())
				.when(userRepo)
				.findByUsername("Jojo");
		
		boolean isUserCreated = userService.addUser(user);
		
		Assert.assertFalse(isUserCreated);
		
		
		Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
		Mockito.verify(mailSender, Mockito.times(0))
				.send(
						ArgumentMatchers.anyString(),
						ArgumentMatchers.anyString(),
						ArgumentMatchers.anyString()
				);
	}
	
	@Test
	void activateUserTest() {
		User user = new User();
		user.setActivationCode("Bingo");
		
		Mockito.doReturn(user)
				.when(userRepo)
				.findByActivationCode("activate");
		
		boolean activated = userService.activateUser("activate");
		
		Assert.assertTrue(activated);
		Assert.assertNull(user.getActivationCode());
		
		
		Mockito.verify(userRepo, Mockito.times(1)).save(user);
		
	}
	
	@Test
	void activateUserFailTest() {
		boolean activated = userService.activateUser("activate");
		
		Assert.assertFalse(activated);
		
		Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
		
	}
}