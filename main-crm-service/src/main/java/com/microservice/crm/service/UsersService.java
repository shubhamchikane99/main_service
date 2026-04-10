package com.microservice.crm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservice.crm.entity.Users;
import com.microservice.crm.repository.UsersRepository;

@Service
public class UsersService {

	@Autowired
	private UsersRepository usersRepository;

	public List<Users> getUserAll() {

		return usersRepository.findAll();
	}
}
