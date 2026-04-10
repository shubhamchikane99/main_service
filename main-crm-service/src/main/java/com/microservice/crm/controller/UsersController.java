package com.microservice.crm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.crm.entity.Users;
import com.microservice.crm.exception.ResourceNotFoundException;
import com.microservice.crm.service.UsersService;

@RestController
@RequestMapping("v1/users")
public class UsersController {

	@Autowired
	private UsersService usersService;

	@GetMapping("/get-all")
	public List<Users> getUserAll() throws ResourceNotFoundException {

		return usersService.getUserAll();
	}

}
