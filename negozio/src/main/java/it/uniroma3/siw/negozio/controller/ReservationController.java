package it.uniroma3.siw.negozio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.negozio.service.ReservationService;

@Controller
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/reservations")
    public String getReservations(Model model) {
        model.addAttribute("reservations", this.reservationService.findAll());
        return "reservations.html";
    }
}