package com.apap.tutorial8.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apap.tutorial8.model.UserRoleModel;

public interface UserRoleDB extends JpaRepository<UserRoleModel, Long>{
	UserRoleModel findByUsername(String username);
}
