package com.lp.tontine.project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lp.tontine.project.entities.User;

@Repository
public interface LoginRepository extends JpaRepository<User, Long>{
	User findByEmailAndPassword(String email, String password);
	User findByEmail(String email);
	
}
