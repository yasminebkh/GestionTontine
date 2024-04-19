package com.lp.tontine.project.web;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lp.tontine.project.Repository.OperationDartRepository;
import com.lp.tontine.project.Repository.ParticipantRepository;
import com.lp.tontine.project.entities.Admin;
import com.lp.tontine.project.entities.Cotisation;
import com.lp.tontine.project.entities.OperationDart;
import com.lp.tontine.project.entities.Participant;
import com.lp.tontine.project.metier.CotisationMetier;
import com.lp.tontine.project.metier.OperationDartMetier;
import com.lp.tontine.project.metier.ParticipantMetier;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/dart")
public class OperationDartController {
	
	@Autowired
	private OperationDartMetier operationdartMetier ;
	
	@Autowired
	private CotisationMetier cotisationMetier;
	
	@Autowired
	private ParticipantMetier participantMetier;
	
	
	
//--------------------------Participant----------------------------------	
	 @GetMapping("/participant/home")
	 public String getAllOperations(Model model, HttpServletRequest request) {
		 Participant participant = (Participant) request.getSession().getAttribute("loggedInParticipant");
		 List<OperationDart> operationDarts = operationdartMetier.getAllOperations();
		 List<OperationDart> UnjoinedDarts = new ArrayList<>();
		 for(OperationDart darts:operationDarts) {
		     boolean isJoined = cotisationMetier.checkIfParticipantJoined(participant, darts);
		     if(!isJoined) {
		         UnjoinedDarts.add(darts);
		     }
		 }
		 if(participant.getRecoit()==1) {
			 request.getSession().setAttribute("notificationSent", true);
			 boolean attribut = (boolean) request.getSession().getAttribute("notificationSent");
			 model.addAttribute("notificationSent",attribut);
			 operationdartMetier.updateReceived(participant);
		 }
		 model.addAttribute("loggedInParticipant", participant);
		 model.addAttribute("operationDarts", UnjoinedDarts);
	     return "ParticipantHome"; 
	 }
	 
	 @GetMapping("/participant/home/Mydarts/{id}")
	 public String showMyDartsPage(@PathVariable("id") Long participantId ,Model model,HttpServletRequest request) {
		 Participant participant = (Participant) request.getSession().getAttribute("loggedInParticipant");
		 List<OperationDart> participantDarts = operationdartMetier.getAllMyDarts(participantId);
	     model.addAttribute("dartsList", participantDarts);
	     model.addAttribute("loggedInParticipant", participant);
	     HttpSession session = request.getSession();
	     model.addAttribute("activerButtonPayer", session.getAttribute("activerButton"));
	     return "Mydarts"; 
	 }
	 
	 @GetMapping("/participant/home/joinDart/{id}")
	 public String joinDartForm(@PathVariable("id") Long dartId,Model model,HttpServletRequest request) {
		 Participant participant = (Participant) request.getSession().getAttribute("loggedInParticipant");
		 OperationDart dart = operationdartMetier.getDartbyId(dartId);
		 model.addAttribute("loggedInParticipant", participant);
		 model.addAttribute("dartTojoin", dart);
		return "joinDart";
	 }
	 
	 @PostMapping("/participant/home/joinDart")
	 public String joinDartForm(@ModelAttribute("joinDart") Cotisation cotisation,RedirectAttributes redirectAttributes){
		 System.out.print(cotisation);
		 operationdartMetier.joinDart(cotisation);
		 redirectAttributes.addFlashAttribute("successMessage", "You are now successfully participating in this dart !");
		 return "redirect:/dart/participant/home";
	 }

	 @GetMapping("/participant/home/CheckDart/{id}")
	 public String ChechDart (@PathVariable("id") Long dartId,Model model) {
		 OperationDart dart = operationdartMetier.getDartbyId(dartId);
		 List<Participant> participants = operationdartMetier.getParticipantCotiser(dartId);
		 model.addAttribute("participantsList", participants);
		 model.addAttribute("dart", dart);
		 return "CheckDart";
	 }
	 
	 @GetMapping("/participant/home/account")
	 public String Account(Model model,HttpServletRequest request) {
		 Participant participant = (Participant) request.getSession().getAttribute("loggedInParticipant");
		 model.addAttribute("loggedInParticipant", participant);
		 return "Account";
	 }

	 @PostMapping("/participantsPayed/{participantId}/{dartId}")
	 public ResponseEntity<String> participantsPayed(@PathVariable Long participantId, @PathVariable Long dartId) {
	     cotisationMetier.participantsPayed(participantId, dartId);
	     return ResponseEntity.ok("Paiement effectué pour le participant ID " + participantId + " pour la Dart ID " + dartId);
	 }
	 
//	 @PostMapping("/receive-amount")
//	    public ResponseEntity<String> receiveAmount() {
//	        System.out.println("Amount received with success");
//	        return ResponseEntity.ok("Amount received with success");
//	    }
	 
//---------------------------Admin---------------------------------------
	 
	 @GetMapping("/admin/home")
	 public String getAdminHome(Model model, HttpServletRequest request) {
		 Admin admin = (Admin) request.getSession().getAttribute("loggedInAdmin");
		 List<OperationDart> operationDarts = operationdartMetier.getAllOperations();
		 model.addAttribute("loggedInParticipant", admin);
		 model.addAttribute("operationDarts", operationDarts);
	     return "AdminHome"; 
	 }
	 
	 @InitBinder
	 public void initBinder(WebDataBinder binder) {
	     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	     sdf.setLenient(true);
	     binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
	 }
	 @GetMapping("/admin/create")
	 public String createDart(Model model) {
		 model.addAttribute("dart", new OperationDart());
	     return "CreateDart"; 
	 }
	 @PostMapping("/admin/create")
	 public String postCreateDart(@ModelAttribute("dart") OperationDart dart ,Model model ,BindingResult errors , HttpServletRequest request,RedirectAttributes redirectAttributes) {
			if (errors.hasErrors()) {
				for (ObjectError error : errors.getAllErrors()) {
			        System.out.println("Erreur: " + error.getDefaultMessage());
			    }
				request.getSession().setAttribute("ERROR", true);
				return "CreateDarte" ;
			}
			if (dart.getMontant()==null || dart.getNombre_personne()==0 ||dart.getPas_tour_role()==null) {
	    		System.out.println("try to insert a null Dart !!");
	    		redirectAttributes.addFlashAttribute("nullInsert", "try to insert a null Dart !!");
	    	}else {
	    		dart.setFlag("Inactive");
	    		System.out.println(dart.getFlag());
	    		operationdartMetier.create(dart);
	    		redirectAttributes.addFlashAttribute("success", "A new Operation Dart has been successfully created");
	    		return "redirect:/dart/admin/home";
	    	}
			
		 return "redirect:/dart/admin/home"; 
	 }
	 
	 @GetMapping("/admin/list/{id}")
	 public String listParticipantsOfDart(@PathVariable("id") Long dartId,Model model) {
		 OperationDart dart = operationdartMetier.getDartbyId(dartId);
		 List<Participant> participants = operationdartMetier.getParticipantCotiser(dartId);
		 model.addAttribute("participantsList", participants);
		 model.addAttribute("dart",dart);
	     return "ListParticipantOfDart"; 
	 }
	 
	 @GetMapping("/admin/ListParticipants")
	 public String listParticipants(Model model, HttpServletRequest request) {
		 List<Participant> participants = participantMetier.getAllParticipants();
		 model.addAttribute("participantsList", participants);
	     return "ListParticipants"; 
	 }
	
	 @GetMapping("/admin/ListDarts")
	 public String listDarts(Model model, HttpServletRequest request) {
	     return "ListDarts"; 
	 }
	 
	 @GetMapping("/admin/edit/{id}")
	 public String Edit(@PathVariable("id") Long dartId,Model model, HttpServletRequest request) {
		 OperationDart dart = operationdartMetier.getDartbyId(dartId);
	     model.addAttribute("dart", dart);
	     return "Edit"; 
	 }
	 @PostMapping("/admin/edit/{id}")
	 public String postEdit(@PathVariable("id") Long id, @ModelAttribute("dart") OperationDart updatedDart,Model model, HttpServletRequest request) {
		 operationdartMetier.editDart(id, updatedDart);
		 return "redirect:/dart/admin/home"; 
	 }
	 @PostMapping("/admin/home/{id}")
		public String deleteOperationDart(@PathVariable("id") Long id) {
		 operationdartMetier.deleteDart(id);
			return "redirect:/dart/admin/home";
		}
}
