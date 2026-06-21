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

    private final CDService cdService;
    private final it.uniroma3.siw.negozio.service.AuthorService authorService;
    private final it.uniroma3.siw.negozio.validation.CDValidator cdValidator;

    public CDController(CDService cdService, it.uniroma3.siw.negozio.service.AuthorService authorService, it.uniroma3.siw.negozio.validation.CDValidator cdValidator) {
        this.cdService = cdService;
        this.authorService = authorService;
        this.cdValidator = cdValidator;
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

    @GetMapping("/admin/cds/new")
    public String createForm(Model model) {
        model.addAttribute("cd", new CD());
        model.addAttribute("authors", authorService.findAll());
        return "admin/cds/formCD";
    }

    @org.springframework.web.bind.annotation.PostMapping("/admin/cds")
    public String save(@jakarta.validation.Valid @org.springframework.web.bind.annotation.ModelAttribute("cd") CD cd,
            org.springframework.validation.BindingResult bindingResult, Model model) {
        cdValidator.validate(cd, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.findAll());
            return "admin/cds/formCD";
        }
        cdService.save(cd);
        return "redirect:/cds/" + cd.getId();
    }

    @GetMapping("/admin/cds/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Optional<CD> optional = cdService.findById(id);
        if (optional.isEmpty()) {
            return "redirect:/cds";
        }
        model.addAttribute("cd", optional.get());
        model.addAttribute("authors", authorService.findAll());
        return "admin/cds/formCD";
    }

    @org.springframework.web.bind.annotation.PostMapping("/admin/cds/{id}/delete")
    public String delete(@PathVariable Long id) {
        cdService.deleteById(id);
        return "redirect:/cds";
    }
}