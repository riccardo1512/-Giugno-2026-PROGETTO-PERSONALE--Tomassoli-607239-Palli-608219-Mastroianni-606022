package it.uniroma3.siw.negozio.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.negozio.model.CD;
import it.uniroma3.siw.negozio.model.Credentials;
import it.uniroma3.siw.negozio.model.Review;
import it.uniroma3.siw.negozio.model.User;
import it.uniroma3.siw.negozio.service.CDService;
import it.uniroma3.siw.negozio.service.CredentialsService;
import it.uniroma3.siw.negozio.service.ReviewService;
import jakarta.validation.Valid;

import java.util.Optional;

@Controller
public class ReviewController {

    private final ReviewService reviewService;
    private final CDService cdService;
    private final CredentialsService credentialsService;

    public ReviewController(ReviewService reviewService, CDService cdService, CredentialsService credentialsService) {
        this.reviewService = reviewService;
        this.cdService = cdService;
        this.credentialsService = credentialsService;
    }

    @GetMapping("/cds/{id}/reviews")
    public String showReviewsStandard(@PathVariable("id") Long id, Model model) {
        Optional<CD> optional = cdService.findById(id);
        if (optional.isEmpty()) {
            return "redirect:/cds";
        }
        CD cd = optional.get();
        model.addAttribute("cd", cd);
        model.addAttribute("review", new Review());
        return "reviews/reviews.html";
    }

    @PostMapping("/cds/{cdId}/reviews")
    public String newReviewStandard(@Valid @ModelAttribute("review") Review review, BindingResult bindingResult, @PathVariable("cdId") Long cdId, Model model) {
        Optional<CD> optional = cdService.findById(cdId);
        if (optional.isEmpty()) {
            return "redirect:/cds";
        }
        CD cd = optional.get();
        
        if (!bindingResult.hasErrors()) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                Credentials credentials = credentialsService.getCredentials(username);
                User user = credentials.getUser();
                review.setAuthor(user);
                review.setCd(cd);
                reviewService.save(review);
                return "redirect:/cds/" + cd.getId() + "/reviews";
            }
        }
        model.addAttribute("cd", cd);
        return "reviews/reviews.html";
    }

    @GetMapping("/cds/{id}/react-reviews")
    public String showReviewsReact(@PathVariable("id") Long id, Model model) {
        Optional<CD> optional = cdService.findById(id);
        if (optional.isEmpty()) {
            return "redirect:/cds";
        }
        CD cd = optional.get();
        model.addAttribute("cd", cd);
        return "reviews/reactReviews.html";
    }
}
