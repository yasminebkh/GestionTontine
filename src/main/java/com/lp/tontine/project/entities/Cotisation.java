package com.lp.tontine.project.entities;


import java.sql.Date;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity     
public class Cotisation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Double montant_cotise;
	private Date date_participation;
	//private Date date_virement;
	@ManyToOne
	private OperationDart operationDart;
	@ManyToOne
	private Participant participant;
	
	public Cotisation(Long id, Double montant_cotise,Date date_participation) {
		super();
		this.id = id;
		this.montant_cotise = montant_cotise;
		this.date_participation=date_participation;
	}

	public Cotisation() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getMontant_cotise() {
		return montant_cotise;
	}

	public void setMontant_cotise(Double montant_cotise) {
		this.montant_cotise = montant_cotise;
	}

	public OperationDart getOperationDart() {
		return operationDart;
	}

	public void setOperationDart(OperationDart operationDart) {
		this.operationDart = operationDart;
	}

	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
	}
	
	public Date getDate_participation() {
		return date_participation;
	}

	public void setDate_participation(Date date_participation) {
		this.date_participation = date_participation;
	}

	@Override
	public String toString() {
		return "Cotisation [id=" + id + ", montant_cotise=" + montant_cotise + ", operationDart=" + operationDart
				+ ", participant=" + participant + "Date date_participation=" +date_participation +"]";
	}

	
	
	
	
}
