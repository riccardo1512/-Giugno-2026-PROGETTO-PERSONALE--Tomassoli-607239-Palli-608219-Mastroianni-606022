package it.uniroma3.siw.negozio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.ui.Model;
import it.uniroma3.siw.negozio.service.CDService;

@Controller
public class HomeController {

    private final CDService cdService;

    public HomeController(CDService cdService) {
        this.cdService = cdService;
    }

    @GetMapping("/")
    public String getHome(Model model) {
        model.addAttribute("cds", cdService.findAll());
        return "index";
    }
}
