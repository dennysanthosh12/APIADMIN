package com.TCS.EIS.apiadmin.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.TCS.EIS.apiadmin.Model.User;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);
	
	boolean existsByUsername(String username);
}
