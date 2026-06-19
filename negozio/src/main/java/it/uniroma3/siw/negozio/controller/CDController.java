package it.uniroma3.siw.negozio.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.negozio.model.CD;
import it.uniroma3.siw.negozio.service.CDService;

@Controller
public class CDController {

    private CDService cdService;

    public CDController(CDService cdService) {
        this.cdService = cdService;
    }

    @GetMapping("/cds")
    public String getCDs(Model model) {
        model.addAttribute("cds", this.cdService.findAll());
        return "cds/listCD";
    }

    @GetMapping("/cds/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        Optional<CD> optional = cdService.findById(id);
        if (optional.isEmpty()) {
            return "redirect:/cds";
        }
        model.addAttribute("cd", optional.get());
        return "cds/showCD";
    }
}