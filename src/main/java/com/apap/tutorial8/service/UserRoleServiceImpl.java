package com.apap.tutorial8.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.apap.tutorial8.model.UserRoleModel;
import com.apap.tutorial8.repository.UserRoleDB;

@Service
public class UserRoleServiceImpl implements UserRoleService{
	@Autowired
	private UserRoleDB userDb;

	@Override
	public UserRoleModel addUser(UserRoleModel user) {
		String pass = encrypt(user.getPassword());
		user.setPassword(pass);
		return userDb.save(user);
	}

	@Override
	public String encrypt(String password){
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(password);
		return hashedPassword;
	}
	
	@Override
	public UserRoleModel findUserByUsername(String username) {
		return userDb.findByUsername(username);
	}
	
	@Override
	public void changePassword(UserRoleModel user, String newPassword) {
		String pass = encrypt(newPassword);
		user.setPassword(pass);
		userDb.save(user);
		
	}
}
