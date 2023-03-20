package com.qxm.payment.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.qxm.payment.domain.model.entity.User;
import com.qxm.payment.domain.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = repository.findByUsername(username);
		UserBuilder builder = null;
		if (user.isPresent()) {
			//PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
			//PasswordEncoder encoder = new BCryptPasswordEncoder();
			
			User currentUser = user.get();
			builder = org.springframework.security.core.userdetails.User.withUsername(username);
			builder.password(currentUser.getPassword());
			builder.roles("USER");
		} else {
			throw new UsernameNotFoundException("User not found.");
		}
		return builder.build();
	}

}
