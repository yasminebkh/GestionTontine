package com.lp.tontine.project.metier;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.lp.tontine.project.Repository.ParticipantRepository;
import com.lp.tontine.project.entities.Participant;

@Component
@Service
public class ParticipantMetier {

	@Autowired
	private ParticipantRepository repository;
	
	public Participant getParticipantDetails(String email) {
		return repository.findByEmail(email) ;
	}
	public List<Participant> getAllParticipants(){
		return repository.findAll();
	}
}
