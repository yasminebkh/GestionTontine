package com.lp.tontine.project.entities;

import java.time.LocalDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="User")
public class User {
	
	@Id()
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank(message = "Champ obligatoire !!! ")
    private String nom;
	@NotBlank(message = "Champ obligatoire !!! ")
    private String prenom;
	@NotEmpty(message = "L'adresse e-mail ne peut pas être vide")
    @Email(message = "L'adresse e-mail doit être valide")
    private String email;
	@NotEmpty(message = "Le mot de passe ne peut pas être vide")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).*$",
        message = "Le mot de passe doit respecter les critères de complexité")
	private String password;
	private String confirmpassword;
    private LocalDate dateNaiss;
    private String adresse;
    @NotBlank(message = "Champ obligatoire !!! ")
    private String numTel;
    private String profession;
    private String sexe;
    private String role="ROLE_USER";
    
    
	public User(Long id, String nom, String prenom, String email, LocalDate dateNaiss, String adresse, String numTel,
			String profession, String password,String confirmpassword , String sexe, Participant id_participant) {
		super();
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.dateNaiss = dateNaiss;
		this.adresse = adresse;
		this.numTel = numTel;
		this.profession = profession;
		this.password = password;
		this.confirmpassword=confirmpassword;
		this.sexe = sexe;
	}
	public User() {
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSexe() {
		return sexe;
	}
	public void setSexe(String sexe) {
		this.sexe = sexe;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getConfirmpassword() {
		return confirmpassword;
	}
	public void setConfirmpassword(String confirmpassword) {
		this.confirmpassword = confirmpassword;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", nom=" + nom + ", prenom=" + prenom + ", email=" + email + ", dateNaiss="
				+ dateNaiss + ", adresse=" + adresse + ", numTel=" + numTel + ", profession=" + profession
				+ ", password=" + password + ", Comfirmpassword=" + confirmpassword + ", sexe=" + sexe + "]";
	}
	

}
