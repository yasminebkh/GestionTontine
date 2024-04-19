package com.lp.tontine.project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lp.tontine.project.entities.Participant;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long>{
	
	Participant findByEmail(String email);
	
	
}
