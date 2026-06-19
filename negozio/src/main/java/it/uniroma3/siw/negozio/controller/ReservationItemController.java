package it.uniroma3.siw.negozio.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.negozio.model.ReservationItem;
import it.uniroma3.siw.negozio.service.ReservationItemService;

@Controller
public class ReservationItemController {

    private ReservationItemService reservationItemService;

    public ReservationItemController(ReservationItemService reservationItemService) {
        this.reservationItemService = reservationItemService;
    }

    @GetMapping("/reservationItems")
    public String getReservationItems(Model model) {
        model.addAttribute("reservationItems", this.reservationItemService.findAll());
        return "reservationItems/listReservationItem";
    }

    @GetMapping("/reservationItems/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        Optional<ReservationItem> optional = reservationItemService.findById(id);
        if (optional.isEmpty()) {
            return "redirect:/reservationItems";
        }
        model.addAttribute("reservationItem", optional.get());
        return "reservationItems/showReservationItem";
    }
}
