package com.garyljy.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.garyljy.ppmtool.domain.User;
import com.garyljy.ppmtool.exceptions.UsernameAlreadyExistException;
import com.garyljy.ppmtool.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public User saveUser(User newUser) {
		try {
			newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
			
			//Username has to be unique(Exception)
			newUser.setUsername(newUser.getUsername());
			
			//Make sure that password and confirmPassword match
			//We don't persist or show the confirmPassword
			newUser.setConfirmPassword("");
			return userRepository.save(newUser);
		} catch (Exception e) {
			// TODO: handle exception
			throw new UsernameAlreadyExistException("Username '" + newUser.getUsername() + "' already exists");
		}
		
	}
}
