package com.lp.tontine.project.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;


@Entity
public class Participant {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private LocalDate dateNaiss;
    private String adresse;
    private String numTel;
    private String profession;
    private String sexe;
    private String role = "ROLE_PARTICIPANT";
    private String Enattente;
    private int recevoir ;
    private Date datePayement;
    private Double SoldeTotal ;
  
    //fetch = FetchType.EAGER ,
    @OneToMany(mappedBy = "participant" ,cascade = CascadeType.ALL)
    private Collection<Cotisation> cotisations = new ArrayList<>();

    
	public Participant(Long id, String nom, String prenom, String email, LocalDate dateNaiss, String adresse,
			String numTel, String profession, String sexe, Double montant, String Enattente, int recevoir) {
		super();
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.dateNaiss = dateNaiss;
		this.adresse = adresse;
		this.numTel = numTel;
		this.profession = profession;
		this.sexe = sexe;
		this.Enattente = Enattente;
		this.recevoir= recevoir;
	}
	public Participant() {
		super();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public LocalDate getDateNaiss() {
		return dateNaiss;
	}
	public void setDateNaiss(LocalDate dateNaiss) {
		this.dateNaiss = dateNaiss;
	}
	public String getAdresse() {
		return adresse;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	public String getNumTel() {
		return numTel;
	}
	public void setNumTel(String numTel) {
		this.numTel = numTel;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getSexe() {
		return sexe;
	}
	public void setSexe(String sexe) {
		this.sexe = sexe;
	}

	@Override
	public String toString() {
		return "Participant [id=" + id + ", nom=" + nom + ", prenom=" + prenom + ", email=" + email + ", dateNaiss="
				+ dateNaiss + ", adresse=" + adresse + ", numTel=" + numTel + ", profession=" + profession + ", sexe="
				+ sexe + ", payed=" + Enattente + "recevoir"+recevoir+ "]";
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public String getEnattente() {
		return Enattente;
	}
	public void setEnattente(String Enattente) {
		this.Enattente = Enattente;
	}
	public int getRecoit() {
		return recevoir;
	}
	public void setRecoit(int recoit) {
		this.recevoir = recoit;
	}
	public Date getDatePayement() {
		return datePayement;
	}
	public void setDatePayement(Date datePayement) {
		this.datePayement = datePayement;
	}
	public Double getSoldeTotal() {
		return SoldeTotal;
	}
	public void setSoldeTotal(Double soldeTotal) {
		SoldeTotal = soldeTotal;
	}
    
    
    
}
