package it.uniroma3.siw.negozio.controller;

import java.util.Optional;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.negozio.model.Credentials;
import it.uniroma3.siw.negozio.model.Reservation;
import it.uniroma3.siw.negozio.model.User;
import it.uniroma3.siw.negozio.service.CredentialsService;
import it.uniroma3.siw.negozio.service.ReservationService;

@Controller
public class ReservationController {

    private ReservationService reservationService;
    private CredentialsService credentialsService;

    public ReservationController(ReservationService reservationService, CredentialsService credentialsService) {
        this.reservationService = reservationService;
        this.credentialsService = credentialsService;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            Object principal = authentication.getPrincipal();
            String username = null;
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else if (principal instanceof OAuth2User) {
                OAuth2User oauth2User = (OAuth2User) principal;
                username = oauth2User.getAttribute("email");
                if (username == null) username = oauth2User.getAttribute("login") + "@github.com";
            }
            if (username != null) {
                Credentials credentials = credentialsService.getCredentials(username);
                if (credentials != null) return credentials.getUser();
            }
        }
        return null;
    }

    @GetMapping("/reservations")
    public String getReservations(Model model) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("reservations", this.reservationService.findByUser(currentUser));
        return "reservations/listReservation";
    }

    @GetMapping("/reservations/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        Optional<Reservation> optional = reservationService.findById(id);
        if (optional.isEmpty()) {
            return "redirect:/reservations";
        }
        
        Reservation reservation = optional.get();
        User currentUser = getCurrentUser();
        
        boolean isAdmin = false;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals(Credentials.ADMIN_ROLE));
        }

        if (currentUser == null || (!reservation.getUser().equals(currentUser) && !isAdmin)) {
            return "redirect:/reservations";
        }

        model.addAttribute("reservation", reservation);
        return "reservations/showReservation";
    }
}