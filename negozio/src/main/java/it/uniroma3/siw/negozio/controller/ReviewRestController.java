package it.uniroma3.siw.negozio.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.siw.negozio.model.CD;
import it.uniroma3.siw.negozio.model.Credentials;
import it.uniroma3.siw.negozio.model.Review;
import it.uniroma3.siw.negozio.model.User;
import it.uniroma3.siw.negozio.service.CDService;
import it.uniroma3.siw.negozio.service.CredentialsService;
import it.uniroma3.siw.negozio.service.ReviewService;
import jakarta.validation.Valid;

import java.util.Optional;

@RestController
@RequestMapping("/rest")
public class ReviewRestController {

    private final ReviewService reviewService;
    private final CDService cdService;
    private final CredentialsService credentialsService;

    public ReviewRestController(ReviewService reviewService, CDService cdService, CredentialsService credentialsService) {
        this.reviewService = reviewService;
        this.cdService = cdService;
        this.credentialsService = credentialsService;
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
    public Review newReviewReact(@Valid @RequestBody Review review, @PathVariable("cdId") Long cdId) {
        Optional<CD> optional = cdService.findById(cdId);
        if (optional.isEmpty()) {
            throw new RuntimeException("CD non trovato");
        }
        CD cd = optional.get();
        
        User user = getAuthenticatedUser();
        if (user != null) {
            review.setAuthor(user);
            review.setCd(cd);
            return reviewService.save(review);
        }
        throw new RuntimeException("Utente non autenticato");
    }

    @org.springframework.web.bind.annotation.DeleteMapping("/cds/{cdId}/reviews/{reviewId}")
    public org.springframework.http.ResponseEntity<?> deleteReviewReact(@PathVariable("cdId") Long cdId, @PathVariable("reviewId") Long reviewId) {
        Optional<Review> reviewOpt = reviewService.findById(reviewId);
        if (reviewOpt.isPresent()) {
            Review review = reviewOpt.get();
            User currentUser = getAuthenticatedUser();
            if (currentUser != null && (review.getAuthor().getId().equals(currentUser.getId()) || isAdmin())) {
                reviewService.delete(review);
                return org.springframework.http.ResponseEntity.ok().build();
            }
            return org.springframework.http.ResponseEntity.status(403).body("Non autorizzato");
        }
        return org.springframework.http.ResponseEntity.notFound().build();
    }
}
