package it.uniroma3.siw.negozio.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.negozio.model.Reservation;
import it.uniroma3.siw.negozio.service.ReservationService;

@Controller
public class ReservationController {

    private ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public String getReservations(Model model) {
        model.addAttribute("reservations", this.reservationService.findAll());
        return "reservations/listReservation";
    }

    @GetMapping("/reservations/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        Optional<Reservation> optional = reservationService.findById(id);
        if (optional.isEmpty()) {
            return "redirect:/reservations";
        }
        model.addAttribute("reservation", optional.get());
        return "reservations/showReservation";
    }
}