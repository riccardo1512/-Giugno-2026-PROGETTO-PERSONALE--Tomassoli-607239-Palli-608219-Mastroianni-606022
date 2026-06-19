package it.uniroma3.siw.negozio.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.negozio.model.Author;
import it.uniroma3.siw.negozio.service.AuthorService;

@Controller
public class AuthorController {

    private AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/authors")
    public String getAuthors(Model model) {
        model.addAttribute("authors", this.authorService.findAll());
        return "authors/listAuthor";
    }

    @GetMapping("/authors/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        Optional<Author> optional = authorService.findById(id);
        if (optional.isEmpty()) {
            return "redirect:/authors";
        }
        model.addAttribute("author", optional.get());
        return "authors/showAuthor";
    }
}
