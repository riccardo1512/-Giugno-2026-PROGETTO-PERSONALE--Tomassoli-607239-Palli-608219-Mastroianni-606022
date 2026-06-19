package it.uniroma3.siw.negozio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.negozio.service.CDService;

@Controller
public class CDController {

    @Autowired
    private CDService cdService;

    @GetMapping("/cds")
    public String getCDs(Model model) {
        model.addAttribute("cds", this.cdService.findAll());
        return "cds.html";
    }
}