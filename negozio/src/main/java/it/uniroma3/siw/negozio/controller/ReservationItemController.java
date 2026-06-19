package it.uniroma3.siw.negozio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.negozio.service.ReservationItemService;

@Controller
public class ReservationItemController {

    private ReservationItemService reservationItemService;

    @GetMapping("/reservationItems")
    public String getReservationItems(Model model) {
        model.addAttribute("reservationItems", this.reservationItemService.findAll());
        return "reservationItems.html";
    }
}
