package com.lp.tontine.project.metier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.lp.tontine.project.Repository.AdminRepository;
import com.lp.tontine.project.entities.Admin;

@Component
@Service
public class AdminMetier {
	
	@Autowired
	private AdminRepository adminRepository;
	
	public Admin getAdminDetails(String email) {
		return adminRepository.findByEmail(email) ;
	}

}
