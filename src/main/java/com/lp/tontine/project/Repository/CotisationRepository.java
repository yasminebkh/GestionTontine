package com.lp.tontine.project.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lp.tontine.project.entities.Cotisation;
import com.lp.tontine.project.entities.OperationDart;
import com.lp.tontine.project.entities.Participant;

@Repository
public interface CotisationRepository extends JpaRepository<Cotisation, Long>{
	
	List<Cotisation> findCotisationsByParticipantId(Long participantId);

	boolean existsByParticipantAndOperationDart(Participant participant, OperationDart operationDart);

	List<Cotisation> findCotisationsByOperationDartId(Long dartId);

	//tester s'il est participer par montant total ou moitier
	Optional<Cotisation> findByParticipantAndOperationDart(Participant participant, OperationDart operationDart);

	List<Cotisation> findParticipantsIdByOperationDartId(Long dartID);

	//Double findMontant_cotiseByParticipant(Participant p);
//	Cotisation findCotisationByParticipantId(Long participantId);

	Cotisation findCotisationByParticipantIdAndOperationDartId(Long id, Long dartId);
	
	

	
	

}
