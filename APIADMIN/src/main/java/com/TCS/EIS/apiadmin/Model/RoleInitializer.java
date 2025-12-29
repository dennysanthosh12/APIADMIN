package com.TCS.EIS.apiadmin.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.TCS.EIS.apiadmin.Repository.RoleRepository;

@Component
public class RoleInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) {

        if (roleRepository.findByRole(Role.Erole.ROLE_USER).isEmpty()) {
            roleRepository.save(new Role(null, Role.Erole.ROLE_USER));
        }

        if (roleRepository.findByRole(Role.Erole.ROLE_ADMIN).isEmpty()) {
            roleRepository.save(new Role(null, Role.Erole.ROLE_ADMIN));
        }
    }
}

