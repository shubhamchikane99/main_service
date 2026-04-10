package com.microservice.crm.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/check")
public class HealthController {

	@GetMapping("/ok")
	public String helthCheck() {

		return "helth is ok";
	}

}
