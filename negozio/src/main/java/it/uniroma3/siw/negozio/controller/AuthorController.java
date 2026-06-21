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

    private final AuthorService authorService;
    private final it.uniroma3.siw.negozio.validation.AuthorValidator authorValidator;

    public AuthorController(AuthorService authorService, it.uniroma3.siw.negozio.validation.AuthorValidator authorValidator) {
        this.authorService = authorService;
        this.authorValidator = authorValidator;
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

    @GetMapping("/admin/authors/new")
    public String createForm(Model model) {
        model.addAttribute("author", new Author());
        return "admin/authors/formAuthor";
    }

    @org.springframework.web.bind.annotation.PostMapping("/admin/authors")
    public String save(@jakarta.validation.Valid @org.springframework.web.bind.annotation.ModelAttribute("author") Author author,
            org.springframework.validation.BindingResult bindingResult, Model model) {
        authorValidator.validate(author, bindingResult);
        if (bindingResult.hasErrors()) {
            return "admin/authors/formAuthor";
        }
        authorService.save(author);
        return "redirect:/authors/" + author.getId();
    }

    @GetMapping("/admin/authors/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Optional<Author> optional = authorService.findById(id);
        if (optional.isEmpty()) {
            return "redirect:/authors";
        }
        model.addAttribute("author", optional.get());
        return "admin/authors/formAuthor";
    }

    @org.springframework.web.bind.annotation.PostMapping("/admin/authors/{id}/delete")
    public String delete(@PathVariable Long id, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            authorService.deleteById(id);
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/authors/" + id;
        }
        return "redirect:/authors";
    }

    @org.springframework.web.bind.annotation.PostMapping("/admin/authors/{id}/deleteWithCDs")
    public String deleteWithCDs(@PathVariable Long id) {
        authorService.deleteWithCDs(id);
        return "redirect:/authors";
    }
}
