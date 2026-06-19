package it.uniroma3.siw.negozio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.negozio.service.AuthorService;

@Controller
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @GetMapping("/authors")
    public String getAuthors(Model model) {
        model.addAttribute("authors", this.authorService.findAll());
        return "authors.html";
    }
}