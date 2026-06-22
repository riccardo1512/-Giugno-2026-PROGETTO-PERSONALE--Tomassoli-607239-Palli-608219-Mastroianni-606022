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

    private User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User) {
            org.springframework.security.oauth2.core.user.OAuth2User oauth2User = (org.springframework.security.oauth2.core.user.OAuth2User) principal;
            username = oauth2User.getAttribute("email");
            if (username == null) {
                username = oauth2User.getAttribute("login") + "@github.com";
            }
        }
        if (username != null) {
            Credentials credentials = credentialsService.getCredentials(username);
            if (credentials != null) {
                return credentials.getUser();
            }
        }
        return null;
    }

    private boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(Credentials.ADMIN_ROLE));
    }

    @PostMapping("/cds/{cdId}/reviews")
    public String newReviewStandard(@Valid @ModelAttribute("review") Review review, BindingResult bindingResult, @PathVariable("cdId") Long cdId, Model model) {
        Optional<CD> optional = cdService.findById(cdId);
        if (optional.isEmpty()) {
            return "redirect:/cds";
        }
        CD cd = optional.get();
        
        if (!bindingResult.hasErrors()) {
            User user = getAuthenticatedUser();
            if (user != null) {
                review.setAuthor(user);
                review.setCd(cd);
                reviewService.save(review);
                // Dopo aver salvato la recensione, rimandiamo l'utente alla pagina del CD 
                // così può vedere l'elenco completo delle recensioni (compresa la sua appena inserita)
                return "redirect:/cds/" + cd.getId();
            }
        }
        model.addAttribute("cd", cd);
        return "reviews/reviews.html";
    }

    @GetMapping("/cds/{cdId}/reviews/{reviewId}/delete")
    public String deleteReviewStandard(@PathVariable("cdId") Long cdId, @PathVariable("reviewId") Long reviewId) {
        Optional<Review> reviewOpt = reviewService.findById(reviewId);
        if (reviewOpt.isPresent()) {
            Review review = reviewOpt.get();
            User currentUser = getAuthenticatedUser();
            // Permetti l'eliminazione se l'utente è l'autore della recensione OPPURE è un amministratore
            if (currentUser != null && (review.getAuthor().getId().equals(currentUser.getId()) || isAdmin())) {
                reviewService.delete(review);
            }
        }
        return "redirect:/cds/" + cdId;
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
