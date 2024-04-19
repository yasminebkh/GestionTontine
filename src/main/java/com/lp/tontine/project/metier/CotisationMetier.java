package com.lp.tontine.project.metier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.lp.tontine.project.Repository.CotisationRepository;
import com.lp.tontine.project.Repository.OperationDartRepository;
import com.lp.tontine.project.Repository.ParticipantRepository;
import com.lp.tontine.project.entities.Cotisation;
import com.lp.tontine.project.entities.OperationDart;
import com.lp.tontine.project.entities.Participant;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Component
@Service
public class CotisationMetier {
	
	
	@Autowired
    private HttpServletRequest request;
	@Autowired
	private CotisationRepository cotisationRepository;
	@Autowired
	private OperationDartRepository operationDartRepository;
	@Autowired
	private ParticipantRepository participantRepository;
	
	public boolean checkIfParticipantJoined(Participant participant, OperationDart dart) {
	    return cotisationRepository.existsByParticipantAndOperationDart(participant, dart);
	}
	
	
	public void participantsPayed(Long participantID, Long dartID) {
	    HttpSession session = request.getSession();
        Map<Long, List<Long>> dartParticipantspaid = (Map<Long, List<Long>>) session.getAttribute("dartParticipantspaid");
        if (dartParticipantspaid == null) {
        	dartParticipantspaid = new HashMap<>();
        }

        List<Long> paidParticipantsForDart = dartParticipantspaid.getOrDefault(dartID, new ArrayList<>());

        if (!paidParticipantsForDart.contains(participantID)) {
        	paidParticipantsForDart.add(participantID);
            dartParticipantspaid.put(dartID, paidParticipantsForDart);
            session.setAttribute("dartParticipantspaid", dartParticipantspaid);
        }
	}
		
	//l'admin qui va faire appel a cette methode
	public boolean verificationPayement(Long participantID,Long DartID) {
		OperationDart operation = operationDartRepository.findOperationDartById(DartID);
		HttpSession session = request.getSession();
		Map<Long, List<Long>> dartParticipantspaid = (Map<Long, List<Long>>) session.getAttribute("dartParticipantspaid");
		if (dartParticipantspaid == null) {
			dartParticipantspaid = new HashMap<>();
	    }
		List<Long> participantsPaid = dartParticipantspaid.getOrDefault(DartID, new ArrayList<>());
	    dartParticipantspaid.putIfAbsent(DartID, participantsPaid);
	    //List<Cotisation> allParticipants = cotisationRepository.findParticipantsIdByOperationDartId(DartID);
	    //allParticipants.size()
		//System.out.println("dartParticipantspaid.size() of HashMap<>(): "+ dartParticipantspaid.size()); //dartParticipantspaid.get(DartID).size()
	    if (participantsPaid.size() == operation.getNombre_personne()) {
	        return false ;
	    }
	    return true;
	}
	
	
	
	
	
	
	
//    public Participant tirageAuSortParticipant(Long dartId) {
//    	LocalDate dateActuelle = LocalDate.now();
//    	List<OperationDart> operations = operationDartMetier.getInactiveOperationDart();
//    	for(OperationDart operation : operations) {
//    		// Conversion de LocalDate en Date
//    		ZoneId zoneId = ZoneId.systemDefault();
//            Date dateActuelleConverted = Date.from(dateActuelle.atStartOfDay(zoneId).toInstant());
//    		Date dateDebutDart = operation.getCreated_at();
//    		if(dateDebutDart.equals(dateActuelleConverted)) {
//    			
//    		}
//    		// Récupérer la liste triée des participants de la tontine par date d'intégration
//            //List<Participant> participants = cotisationRepository.findParticipantsByOperationDartIdOrderBydate_participation(dartId);
////            if (!participants.isEmpty()) {
////            	
////                return participants.get(0);
////            } else { 
////                // Gérer le cas où la liste est vide (aucun participant trouvé)
////                return null;
////            }
//    	}
//    	return null;
//        
//    }
    
    public void LancementDart(Participant participant ,Long dartId) {
    	OperationDart operation = operationDartRepository.findOperationDartById(dartId);
    	
    }
    
    public List<Cotisation> sortParticipantByDate(Long idOperation){
    	List<Cotisation> cotisations = cotisationRepository.findCotisationsByOperationDartId(idOperation);
    	// Filtrer les cotisations ayant une date de participation non null
        List<Cotisation> cotisationsNonNullDate = cotisations.stream()
                .filter(cotisation -> cotisation.getDate_participation() != null)
                .collect(Collectors.toList());
        // Comparator pour comparer par la date de participation
        Comparator<Cotisation> dateComparator = Comparator.comparing(Cotisation::getDate_participation);
        // Trier la liste des cotisations en utilisant le comparateur
        List<Cotisation> sortedCotisations = cotisationsNonNullDate.stream()
                .sorted(dateComparator)
                .collect(Collectors.toList());
//        for (Cotisation cotisation : cotisations) {
//			participants.add(cotisation.getParticipant());
//        }
//		return participants;
        return sortedCotisations;
    }
	
}
