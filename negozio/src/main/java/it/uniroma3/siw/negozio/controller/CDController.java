package it.uniroma3.siw.negozio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.negozio.service.CDService;

@Controller
public class CDController {

    private CDService cdService;

    @GetMapping("/cds")
    public String getCDs(Model model) {
        model.addAttribute("cds", this.cdService.findAll());
        return "cds.html";
    }
}