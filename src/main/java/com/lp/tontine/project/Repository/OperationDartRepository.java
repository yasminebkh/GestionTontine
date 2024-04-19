package com.lp.tontine.project.Repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lp.tontine.project.entities.OperationDart;

import jakarta.transaction.Transactional;

@Repository
public interface OperationDartRepository extends JpaRepository<OperationDart, Long>{
	
	//List<OperationDart> findOperationDartsByParticipantId(Long participantId);
	OperationDart findOperationDartById(Long id);
	
	List<OperationDart> findByFlag(String flagValue);
	
	@Modifying
	@Transactional
	@Query("UPDATE OperationDart SET countParticipant = countParticipant + 1 WHERE id = :operationDartId")
	void incrementCountParticipant(@Param("operationDartId") Long operationDartId);
	
	@Modifying
	@Transactional
	@Query("UPDATE OperationDart SET date_debut = :date WHERE id = :operationDartId")
	void updateDate_debut(@Param("date") Date date, @Param("operationDartId") Long operationDartId);

	

}
