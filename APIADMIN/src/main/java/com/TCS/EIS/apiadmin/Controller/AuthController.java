package com.TCS.EIS.apiadmin.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TCS.EIS.apiadmin.Dto.UserWrapper;
import com.TCS.EIS.apiadmin.Services.AuthServices;

@RestController
@RequestMapping("api/Auth")
public class AuthController {

	@Autowired
	private AuthServices services;
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody UserWrapper user){
		try {
			services.register(user);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserWrapper user){
		try {
			return new ResponseEntity<>(services.login(user),HttpStatus.ACCEPTED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
