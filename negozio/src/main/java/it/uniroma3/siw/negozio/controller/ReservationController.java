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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.negozio.model.Credentials;
import it.uniroma3.siw.negozio.model.Reservation;
import it.uniroma3.siw.negozio.model.User;
import it.uniroma3.siw.negozio.model.ReservationState;
import it.uniroma3.siw.negozio.service.CredentialsService;
import it.uniroma3.siw.negozio.service.ReservationService;
import it.uniroma3.siw.negozio.service.CDService;

@Controller
public class ReservationController {

    private ReservationService reservationService;
    private CredentialsService credentialsService;
    private CDService cdService;

    public ReservationController(ReservationService reservationService, CredentialsService credentialsService,
            CDService cdService) {
        this.reservationService = reservationService;
        this.credentialsService = credentialsService;
        this.cdService = cdService;
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
                if (username == null)
                    username = oauth2User.getAttribute("login") + "@github.com";
            }
            if (username != null) {
                Credentials credentials = credentialsService.getCredentials(username);
                if (credentials != null)
                    return credentials.getUser();
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
        model.addAttribute("reservations", this.reservationService.findByUserAndStateNot(currentUser,
                ReservationState.CART));
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
        model.addAttribute("isAdmin", isAdmin);
        return "reservations/showReservation";
    }

    @PostMapping("/reservations/{id}/cancel")
    public String cancelReservation(@PathVariable("id") Long id) {
        User currentUser = getCurrentUser();
        Optional<Reservation> optional = reservationService.findById(id);
        if (currentUser != null && optional.isPresent()) {
            Reservation reservation = optional.get();
            if (reservation.getUser().equals(currentUser)) {
                reservationService.cancelReservation(reservation);
            }
        }
        return "redirect:/reservations/" + id;
    }

    @GetMapping("/admin/reservations/{id}/edit")
    public String editReservationState(@PathVariable("id") Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(Credentials.ADMIN_ROLE));
        if (!isAdmin)
            return "redirect:/";

        Optional<Reservation> optional = reservationService.findById(id);
        if (optional.isEmpty())
            return "redirect:/reservations";

        model.addAttribute("reservation", optional.get());
        model.addAttribute("states", ReservationState.values());
        return "admin/reservations/formReservationState";
    }

    @GetMapping("/admin/reservations")
    public String manageReservations(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(Credentials.ADMIN_ROLE));
        if (!isAdmin)
            return "redirect:/";

        // Prende tutte le prenotazioni tranne quelle ancora nel carrello
        java.util.List<Reservation> allReservations = reservationService.findAll().stream()
            .filter(r -> r.getState() != ReservationState.CART)
            .collect(java.util.stream.Collectors.toList());

        model.addAttribute("reservations", allReservations);
        return "admin/reservations/listReservation";
    }

    @PostMapping("/admin/reservations/{id}/edit")
    public String updateReservationState(@PathVariable("id") Long id,
            @RequestParam("state") ReservationState newState) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(Credentials.ADMIN_ROLE));
        if (!isAdmin)
            return "redirect:/";

        Optional<Reservation> optional = reservationService.findById(id);
        if (optional.isPresent()) {
            Reservation reservation = optional.get();
            reservationService.updateReservationState(reservation, newState);
        }
        return "redirect:/reservations/" + id;
    }

    @PostMapping("/admin/reservations/{id}/delete")
    public String deleteReservation(@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(Credentials.ADMIN_ROLE));
        if (!isAdmin)
            return "redirect:/";

        Optional<Reservation> optional = reservationService.findById(id);
        if (optional.isPresent()) {
            Reservation reservation = optional.get();
            reservationService.deleteById(id);
        }
        return "redirect:/reservations";
    }
}