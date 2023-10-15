package com.bence.mate.spring.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.bence.mate.spring.repository.UserRepository;
import com.bence.mate.spring.entity.User;

import java.util.Optional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// It is automatically exposed to the Security config
@Service
public class UserServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		Optional<User> appUser = userRepo.findByName(name);
		org.springframework.security.core.userdetails.User springUser = null;

		if (appUser.isEmpty()) {
			throw new UsernameNotFoundException("User with name: " + name + " not found");
		} else {
			User user = appUser.get();
			List<String> roles = user.getRoles();
			Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

			for (String role : roles) {
				grantedAuthorities.add(new SimpleGrantedAuthority(role));
			}
			springUser = new org.springframework.security.core.userdetails.User(name, user.getPassword(), grantedAuthorities);
		}
		
		return springUser;
	}
}