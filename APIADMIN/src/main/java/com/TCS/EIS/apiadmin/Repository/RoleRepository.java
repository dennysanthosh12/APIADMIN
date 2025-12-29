package com.TCS.EIS.apiadmin.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.TCS.EIS.apiadmin.Model.Role;
import com.TCS.EIS.apiadmin.Model.Role.Erole;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByRole(Erole role);
}
