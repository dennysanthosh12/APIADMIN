package com.TCS.EIS.apiadmin.Services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.TCS.EIS.apiadmin.Dto.UserWrapper;
import com.TCS.EIS.apiadmin.Model.Role;
import com.TCS.EIS.apiadmin.Model.User;
import com.TCS.EIS.apiadmin.Repository.RoleRepository;
import com.TCS.EIS.apiadmin.Repository.UserRepository;
import com.TCS.EIS.apiadmin.Utility.JwtService;

@Service
public class AuthServices {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtService jwtService;
	
	public void register(UserWrapper user) {
		Role userRole = roleRepository.findByRole(Role.Erole.ROLE_USER)
	            .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));

	        User newUser = new User();
	        newUser.setUsername(user.getUsername());
	        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
	        newUser.setRoles(Set.of(userRole));

	        userRepository.save(newUser);
	}
	
	public String login(UserWrapper request) {

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );

        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return jwtService.generateToken(user);
    }

}
