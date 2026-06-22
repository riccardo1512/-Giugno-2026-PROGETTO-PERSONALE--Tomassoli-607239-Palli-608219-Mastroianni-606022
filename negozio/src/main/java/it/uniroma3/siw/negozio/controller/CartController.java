package it.uniroma3.siw.negozio.controller;

import java.util.Optional;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.negozio.model.CD;
import it.uniroma3.siw.negozio.model.Credentials;
import it.uniroma3.siw.negozio.model.Reservation;
import it.uniroma3.siw.negozio.model.ReservationItem;
import it.uniroma3.siw.negozio.model.ReservationState;
import it.uniroma3.siw.negozio.model.User;
import it.uniroma3.siw.negozio.service.CDService;
import it.uniroma3.siw.negozio.service.CredentialsService;
import it.uniroma3.siw.negozio.service.ReservationItemService;
import it.uniroma3.siw.negozio.service.ReservationService;

@Controller
public class CartController {

    private ReservationService reservationService;
    private ReservationItemService reservationItemService;
    private CredentialsService credentialsService;
    private CDService cdService;

    public CartController(ReservationService reservationService, ReservationItemService reservationItemService, CredentialsService credentialsService, CDService cdService) {
        this.reservationService = reservationService;
        this.reservationItemService = reservationItemService;
        this.credentialsService = credentialsService;
        this.cdService = cdService;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            Object principal = authentication.getPrincipal();
            String username = null;
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else if (principal instanceof OAuth2User) {
                OAuth2User oauth2User = (OAuth2User) principal;
                username = oauth2User.getAttribute("email");
                if (username == null) username = oauth2User.getAttribute("login") + "@github.com";
            }
            if (username != null) {
                Credentials credentials = credentialsService.getCredentials(username);
                if (credentials != null) return credentials.getUser();
            }
        }
        return null;
    }

    @GetMapping("/cart")
    public String showCart(Model model) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }
        Reservation cart = reservationService.getCart(currentUser);
        model.addAttribute("cart", cart);
        
        // Calculate total
        double total = 0.0;
        if (cart.getItems() != null) {
            for (ReservationItem item : cart.getItems()) {
                if (item.getCd() != null) {
                    total += item.getCd().getPrice() * item.getQuantity();
                }
            }
        }
        model.addAttribute("total", total);
        return "cart/showCart";
    }

    @GetMapping("/cart/add/{cdId}")
    public String showAddToCart(@PathVariable("cdId") Long cdId, Model model) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        Optional<CD> cdOpt = cdService.findById(cdId);
        if (cdOpt.isEmpty()) {
            return "redirect:/cds";
        }
        
        model.addAttribute("cd", cdOpt.get());
        return "cart/addToCart";
    }

    @PostMapping("/cart/add/{cdId}")
    public String addToCart(@PathVariable("cdId") Long cdId, @RequestParam("quantity") int quantity) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        Optional<CD> cdOpt = cdService.findById(cdId);
        if (cdOpt.isEmpty()) {
            return "redirect:/cds";
        }
        CD cd = cdOpt.get();
        
        Reservation cart = reservationService.getCart(currentUser);
        
        // Check if item already in cart
        ReservationItem existingItem = null;
        if (cart.getItems() != null) {
            for (ReservationItem item : cart.getItems()) {
                if (item.getCd().getId().equals(cdId)) {
                    existingItem = item;
                    break;
                }
            }
        }
        
        if (existingItem != null) {
            if (existingItem.getQuantity() + quantity > cd.getAvailableQuantity()) {
                return "redirect:/cart?error=stock_exceeded";
            }
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            reservationItemService.save(existingItem);
        } else {
            if (quantity > cd.getAvailableQuantity()) {
                return "redirect:/cart?error=stock_exceeded";
            }
            ReservationItem newItem = new ReservationItem();
            newItem.setCd(cd);
            newItem.setQuantity(quantity);
            newItem.setReservation(cart);
            reservationItemService.save(newItem);
        }
        
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove/{itemId}")
    public String removeFromCart(@PathVariable("itemId") Long itemId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        Optional<ReservationItem> itemOpt = reservationItemService.findById(itemId);
        if (itemOpt.isPresent()) {
            ReservationItem item = itemOpt.get();
            // Verify ownership
            if (item.getReservation() != null && item.getReservation().getUser().equals(currentUser)) {
                reservationItemService.deleteById(itemId);
            }
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/checkout")
    public String checkout() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        Reservation cart = reservationService.getCart(currentUser);
        if (cart.getItems() != null && !cart.getItems().isEmpty()) {
            // First check if all items are in stock
            for (ReservationItem item : cart.getItems()) {
                CD cd = item.getCd();
                if (cd != null && cd.getAvailableQuantity() < item.getQuantity()) {
                    return "redirect:/cart?error=stock_exceeded";
                }
            }
            // Deduct stock
            for (ReservationItem item : cart.getItems()) {
                CD cd = item.getCd();
                if (cd != null) {
                    cd.setAvailableQuantity(cd.getAvailableQuantity() - item.getQuantity());
                    cdService.save(cd);
                }
            }
            cart.setState(ReservationState.PENDING);
            cart.setDate(java.time.LocalDate.now());
            reservationService.save(cart);
        }
        return "redirect:/reservations";
    }
}
