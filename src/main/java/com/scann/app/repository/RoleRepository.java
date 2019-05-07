package com.scann.app.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scann.app.entity.Role;
public interface RoleRepository extends JpaRepository<Role, Long>{
	 Optional<Role> findByRolename(String rolename);
}
