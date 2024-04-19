package com.lp.tontine.project.metier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.lp.tontine.project.Repository.CotisationRepository;
import com.lp.tontine.project.Repository.OperationDartRepository;
import com.lp.tontine.project.Repository.ParticipantRepository;
import com.lp.tontine.project.entities.Cotisation;
import com.lp.tontine.project.entities.OperationDart;
import com.lp.tontine.project.entities.Participant;
import com.lp.tontine.project.web.OperationDartController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Component
@Service
public class OperationDartMetier {
	
	@Autowired
	private OperationDartRepository operationDartRepository;
	private static int currentIndex;
	private static int currentIndexTotal;
	private static int currentIndexMoitier;
	List<Cotisation> participantMoitieeMontant = new ArrayList<>();
	List<Participant> p2 = new ArrayList<Participant>() ;
	@Autowired
    private HttpServletRequest request;
	@Autowired
	ParticipantRepository participantRepository ;
	@Autowired
	private CotisationRepository cotisationRepository;
	@Autowired
	private CotisationMetier cotisationMetier;
	
	public static void initialize() {
		//this.currentParticipantIndex = 0;
		currentIndex=0;
		currentIndexTotal=0;
		currentIndexMoitier=0;
		
	}
	
	public List<OperationDart> getAllOperations() {
        return operationDartRepository.findAll(); 
    }
	
	public List<OperationDart> getInactiveOperationDart() {
		return operationDartRepository.findByFlag("Inactive");
    }
	
	
	public List<OperationDart> getAllMyDarts(Long participantId){
		List<Cotisation> cotisations = cotisationRepository.findCotisationsByParticipantId(participantId);
        List<OperationDart> listDarts = new ArrayList<>();
        for (Cotisation cotisation : cotisations) {
        	listDarts.add(cotisation.getOperationDart());
        }
		return listDarts;
	}
	
	public List<Participant> getParticipantCotiser(Long dartId){
		List<Cotisation> cotisations = cotisationRepository.findCotisationsByOperationDartId(dartId);
		List<Participant> participants = new ArrayList<>();
		for (Cotisation cotisation : cotisations) {
			participants.add(cotisation.getParticipant());
        }
		return participants;
	}
	
	public void joinDart(Cotisation cotisation) {
		OperationDart dart = cotisation.getOperationDart();
		//faire appel a la methode de l'incrementation
		
		if(dart.getMontant().compareTo(Double.valueOf(cotisation.getMontant_cotise()))==0 ) {
			dart.setCountParticipant(dart.getCountParticipant()+1);
			operationDartRepository.save(dart);
			cotisationRepository.save(cotisation);
			//operationDartRepository.incrementCountParticipant(dart.getId());
			if(dart.getNombre_personne()==dart.getCountParticipant()) {
				LocalDate dateActuelle = LocalDate.now();
				ZoneId zoneId = ZoneId.systemDefault();
	            Date dateActuelleConverted = Date.from(dateActuelle.atStartOfDay(zoneId).toInstant());
	            operationDartRepository.updateDate_debut(dateActuelleConverted, dart.getId());
	            System.out.println("Appel de la methode :  => affecterMontantTotalAuPremierParticipant() ");
	            affecterMontantTotalAuPremierParticipant(dart.getId());
			}
		}
		if(cotisation.getMontant_cotise().compareTo(Double.valueOf(dart.getMontant()/2))==0) {
			Participant p = cotisation.getParticipant() ;
			participantMoitieeMontant.add(cotisation);
			HttpSession session = request.getSession();
			session.setAttribute("enAttente_" + p.getId() + "_" + dart.getId(), true);
			if(participantMoitieeMontant.size() == 2) { 
				
				dart.setCountParticipant(dart.getCountParticipant()+1);
				operationDartRepository.save(dart);
				for (int i = 0; i < participantMoitieeMontant.size(); i++) {
					cotisationRepository.save(participantMoitieeMontant.get(i));
					session.setAttribute("enAttente_" +participantMoitieeMontant.get(i).getParticipant().getId()+ "_" + dart.getId(), false);
				}
				participantMoitieeMontant.clear();
				//---------------------------------------------------------
				if(dart.getNombre_personne()==dart.getCountParticipant()) {
					LocalDate dateActuelle = LocalDate.now();
					ZoneId zoneId = ZoneId.systemDefault();
		            Date dateActuelleConverted = Date.from(dateActuelle.atStartOfDay(zoneId).toInstant());
		            operationDartRepository.updateDate_debut(dateActuelleConverted, dart.getId());
		            System.out.println("Appel de la methode :  => affecterMontantTotalAuPremierParticipant() ");
		            affecterMontantTotalAuPremierParticipant(dart.getId());
				}
			}
		}
		
		
		
	}
	

//	public Participant TirageAndactiverButtonPayer(Long dartId) {
//		List<Participant> participants = getParticipantCotiser(dartId);
//		//faire appel a la methode sort
//		//cotisationMetier.sortParticipantByDate(dartId);
//		//participants.sort(Comparator.comparing(Cotisation::getDate_participation));
//		Participant currentParticipant = participants.get(currentParticipantIndex);
//        currentParticipantIndex = (currentParticipantIndex + 1) % participants.size();
////        HttpSession session = request.getSession();
////		session.setAttribute("activerButton",true);
//        return currentParticipant;
//	}
	 
//	public Participant getNextParticipant(Long dartId) {
//		List<Participant> participants = getParticipantCotiser(dartId);
//        if (participants != null && !participants.isEmpty()) {
//            Participant participant = participants.get(0);
//            participants.remove(0);
////            participants.add(participant);
//            System.out.println("participant : => " + participant);
//            System.out.println(participants);
//            System.out.println();
//            return participant;
//        } else {
//            return null ;
//        }
//    }
	//Important
//	public Participant getNextParticipant(Long dartId) {
//		List<Participant> participants = getParticipantCotiser(dartId);
//        if (participants != null && !participants.isEmpty()) {
//            Participant participant = participants.get(currentIndex);
//            currentIndex = (currentIndex + 1) % participants.size();
//            HttpSession session = request.getSession();
//            session.setAttribute("activerButton",true);
//            return participant;
//        } else {
//            return null;
//        }
//    }
	
	 public Participant getNextParticipant(Long dartId) {
		 List<Participant> participants = getParticipantCotiser(dartId);
		 List<Participant> participantsMontantTotal = new ArrayList<Participant>();
		 List<Participant> participantsMoitieMontant = new ArrayList<Participant>();
		 for(Participant p : participants) {
			 Cotisation cotisation = cotisationRepository.findCotisationByParticipantIdAndOperationDartId(p.getId(),dartId);
			 OperationDart dart = operationDartRepository.findOperationDartById(dartId);
			 if(cotisation.getMontant_cotise().compareTo(Double.valueOf(dart.getMontant())) == 0) {
				 participantsMontantTotal.add(p);
			 }else if (cotisation.getMontant_cotise().compareTo(Double.valueOf(dart.getMontant() / 2)) == 0) {
				 //attendEnCour();
				participantsMoitieMontant.add(p);
			}
		 }
	        if (!participantsMontantTotal.isEmpty() || !participantsMoitieMontant.isEmpty()) {
	            if (currentIndexTotal < participantsMontantTotal.size()) {
	                Participant participant = participantsMontantTotal.get(currentIndexTotal);
	                currentIndexTotal++;
	                System.out.println("participant =>");
	    	        System.out.println(participant);
	    	        //HttpSession session = request.getSession();
	    	        //session.setAttribute("activerButton_" + dartId + "_" + participant.getId(), false);
	    	        //System.out.println("setAttribute(\"activerButton_\" => "+ session.getAttribute("activerButton_") +"=>"+ dartId +":"+ participant.getId());
	                return participant;
	            } else if ( currentIndexTotal== participantsMontantTotal.size() && currentIndexMoitier<participantsMoitieMontant.size()) {
	            	Participant participant = participantsMoitieMontant.get(currentIndexMoitier);
	                currentIndexMoitier++;
	                System.out.println("participant =>");
	    	        System.out.println(participant);
	    	        //HttpSession session = request.getSession();
	    	        //session.setAttribute("activerButton_" + dartId + "_" + participant.getId(), false);
	    	        //System.out.println("setAttribute(\"activerButton_\" => "+ session.getAttribute("activerButton_") +"=>"+ dartId +":"+ participant.getId());
	                return participant;
				}
	        }
	        return null;
	    }
	
	//Methode de Lancement 
	public void affecterMontantTotalAuPremierParticipant(Long idOperation) {
		OperationDart dart = operationDartRepository.findOperationDartById(idOperation);
		dart.setFlag("ACTIVE");
		operationDartRepository.save(dart);
		List<Cotisation> sortedCotisations = cotisationMetier.sortParticipantByDate(idOperation);
		Map<String, Long> pasTourRoleToDelayMap = createPasTourRoleToDelayMap();
		ChronoUnit chronoUnit = dart.getPas_tour_role().equals("month") ? ChronoUnit.MONTHS : dart.getPas_tour_role().equals("week") ? ChronoUnit.WEEKS : ChronoUnit.DAYS ;
			LocalDate[] currentDate = {dart.getDate_debut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()};
			ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
			for (int i = 0; i < sortedCotisations.size(); i++) {
				System.out.println("boucle for :");
				scheduler.schedule(() -> {
						Participant participant= getNextParticipant(idOperation);
						Cotisation cotisation = cotisationRepository.findCotisationByParticipantIdAndOperationDartId(participant.getId(),idOperation);
						 OperationDart operationDart = operationDartRepository.findOperationDartById(idOperation);
						//le teste du mantont (complet ou la moitier ) pour faire le traitement 
						if(cotisation.getMontant_cotise().compareTo(Double.valueOf(dart.getMontant() / 2)) == 0) {
							p2.add(participant);
							if(p2.size()!=2) {
								Participant parti = getNextParticipant(idOperation);
								p2.add(parti);
							}
							if (p2.size()==2) {
								for(Participant participantList : p2) {
									participantList.setDatePayement(Date.from(currentDate[0].atStartOfDay(ZoneId.systemDefault()).toInstant()));
							        currentDate[0] = currentDate[0].plus(1, chronoUnit);
							        participantList.setSoldeTotal( cotisation.getMontant_cotise()* dart.getNombre_personne());
							        participantList.setRecoit(1);
							        participantRepository.save(participantList);
							        System.out.println("Date paiement pour " + participantList.getNom() + " : " + participantList.getDatePayement());
							        System.out.println("Montant total affecté à " + participantList.getSoldeTotal());
								}
								p2.clear();
							}
							 
						}
				        if(cotisation.getMontant_cotise().compareTo(Double.valueOf(dart.getMontant())) == 0) {
				        	participant.setDatePayement(Date.from(currentDate[0].atStartOfDay(ZoneId.systemDefault()).toInstant()));
					        currentDate[0] = currentDate[0].plus(1, chronoUnit);
					        participant.setSoldeTotal(dart.getMontant() * dart.getNombre_personne());
					        participant.setRecoit(1);
					        participantRepository.save(participant);
					        System.out.println("Date paiement pour " + participant.getNom() + " : " + participant.getDatePayement());
					        System.out.println("Montant total affecté à " + participant.getSoldeTotal());
				        }
				      //appel au methode qui active Button recive
				      //reciveMontantTotal(Long ParticipantId)
				      //une fois clique sur recevoir il affiche ok 
				        
				}, i * pasTourRoleToDelayMap.get(dart.getPas_tour_role()),  TimeUnit.MINUTES);
			}
			scheduler.shutdown();
	}
	private Map<String, Long> createPasTourRoleToDelayMap() {
        Map<String, Long> pasTourRoleToDelayMap = Map.of(
                "month", 30L,  
                "week", 7L, 
                "day", 1L   
        );
        return pasTourRoleToDelayMap;
    }
	
	public OperationDart getDartbyId(Long id) {
		return operationDartRepository.findOperationDartById(id);
	}
	
	public void updateReceived(Participant participant) {
		participant.setRecoit(0);
		participantRepository.save(participant);
	}
	
	//tester s'il est participer par montant total ou moitier
//    public boolean hasFullMontant(Participant participant, OperationDart operationDart) {
//        Optional<Cotisation> optionalCotisation = cotisationRepository.findByParticipantAndOperationDart(participant, operationDart);
//        if (optionalCotisation.isPresent()) {
//            Cotisation cotisation = optionalCotisation.get();
//            return cotisation.getMontant_cotise().equals(operationDart.getMontant());
//        
//        }
//        return false;
//    }
    
    public void create(OperationDart dart) {
    	LocalDate dateActuelle = LocalDate.now();
		ZoneId zoneId = ZoneId.systemDefault();
        Date dateActuelleConverted = Date.from(dateActuelle.atStartOfDay(zoneId).toInstant());
        dart.setCreated_at(dateActuelleConverted);
    	operationDartRepository.save(dart);
    }
    
    public void deleteDart(Long id) {
    	operationDartRepository.deleteById(id);
    }
    public void editDart(Long id, OperationDart updatedDart) {
        OperationDart existingDart = operationDartRepository.findOperationDartById(id);
        if (existingDart != null) {
            existingDart.setMontant(updatedDart.getMontant());
            existingDart.setNombre_personne(updatedDart.getNombre_personne());
            existingDart.setPas_tour_role(updatedDart.getPas_tour_role());
            operationDartRepository.save(existingDart);
        }
    }
    
//    @Scheduled(cron = "0 0 2 * * *") // Exécution tous les jours à 2h du matin
//    @Transactional
//    public void lancerTontineAutomatiquement() {
//        List<OperationDart> tontinesEnAttente = operationDartRepository.findTontinesEnAttente();
//
//        for (OperationDart tontine : tontinesEnAttente) {
//            if ("TONTINE_EN_ATTENTE".equals(tontine.getFlag()) && tontine.getParticipants().size() == tontine.getNombre_personne()) {
//                // Lancez automatiquement la tontine
//                lancerTontine(tontine);
//            }
//            // Ajoutez des conditions supplémentaires au besoin
//        }
//    }

//    @Transactional
//    public void lancerTontine(OperationDart tontine) {
//        // Mettez à jour l'état de la tontine en cours
//        tontine.setFlag("TONTINE_EN_COURS");
//        operationDartRepository.save(tontine);
//
//        // Effectuez le tirage au sort des participants
//        List<Participant> participants = tontine.getParticipants();
//        Collections.shuffle(participants);
//
//        // Distribuez le montant aux participants dans l'ordre du tirage au sort
//        Double montantParParticipant = tontine.getMontant() / tontine.getNombre_personne();
//        for (Participant participant : participants) {
//        	
//            // Vérifiez si le participant a déjà cotisé
//            Optional<Cotisation> cotisationOptional = cotisationRepository.findByParticipantAndOperationDart(participant, tontine);
//
//            if (!cotisationOptional.isPresent()) {
//                // Si le participant n'a pas encore cotisé, ajoutez la cotisation
//                Cotisation cotisation = new Cotisation();
//                cotisation.setMontant_cotise(montantParParticipant);
//                cotisation.setDate_participation(LocalDate.now());
//                cotisation.setOperationDart(tontine);
//                cotisation.setParticipant(participant);
//
//                // Sauvegardez la cotisation
//                cotisationRepository.save(cotisation);
//
//                // Mettez à jour le solde du participant
//                participant.setSolde(participant.getSolde() + montantParParticipant);
//                participantRepository.save(participant);
//            }
//        }
//    }
    
    
    
    
    
    
}
