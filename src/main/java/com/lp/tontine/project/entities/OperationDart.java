package com.lp.tontine.project.entities;



import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name = "operation_dart")
public class OperationDart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Long id;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date date_debut;
	private Date created_at;
	private Double montant ;
	private int nombre_personne;
	private int countParticipant;
	private String flag;
	private String pas_tour_role;
	private List<Long> participant_payed ;
	
	@OneToMany(mappedBy = "operationDart", cascade = CascadeType.ALL)
	private List<Cotisation> cotisations = new ArrayList<>();
	
//	//this One
//	@ManyToOne
//	@JoinColumn(name = "participant_id")
//	private Participant participant;
	
	
	public OperationDart() {
		super();
	}
	public OperationDart(Long id, Date date_debut,  Double montant ,int nombre_personne, String flag,
			String pas_tour_role) {
		super();
		this.id = id;
		this.date_debut = date_debut;
		this.montant=montant;
		this.nombre_personne = nombre_personne;
		this.flag = flag;
		this.pas_tour_role = pas_tour_role;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getDate_debut() {
		return date_debut;
	}
	public void setDate_debut(Date date_debut) {
		this.date_debut = date_debut;
	}
	public int getNombre_personne() {
		return nombre_personne;
	}
	public void setNombre_personne(int nombre_personne) {
		this.nombre_personne = nombre_personne;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getPas_tour_role() {
		return pas_tour_role;
	}
	public void setPas_tour_role(String pas_tour_role) {
		this.pas_tour_role = pas_tour_role;
	}
	public Double getMontant() {
		return montant;
	}
	public void setMontant(Double montant) {
		this.montant = montant;
	}
	
	@Override
	public String toString() {
		return "OperationDart [id=" + id + ", date_debut=" + date_debut 
				+", montant= " + montant +", nombre_personne=" + nombre_personne + " CountParticipant= "+ countParticipant +", flag=" + flag + ", pas_tour_role=" + pas_tour_role + "]";
	}
	public List<Long> getParticipant_payed() {
		return participant_payed;
	}
	public void setParticipant_payed(List<Long> participant_payed) {
		this.participant_payed = participant_payed;
	}
	
	public void addParticipant_payed(Long participantId) {
		if (this.participant_payed == null) {
            this.participant_payed = new ArrayList<>(); 
        }
		this.participant_payed.add(participantId);
	}
	
	public void destroyParticipant_payed() {
		this.participant_payed.clear();
	}
	public int getCountParticipant() {
		return countParticipant;
	}
	public void setCountParticipant(int countParticipant) {
		this.countParticipant = countParticipant;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
}
