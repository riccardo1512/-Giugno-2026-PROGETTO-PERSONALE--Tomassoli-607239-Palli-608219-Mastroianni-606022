package it.uniroma3.siw.negozio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.negozio.model.Reservation;
import it.uniroma3.siw.negozio.model.ReservationItem;
import it.uniroma3.siw.negozio.model.ReservationState;
import it.uniroma3.siw.negozio.model.User;
import it.uniroma3.siw.negozio.model.CD;
import it.uniroma3.siw.negozio.repository.ReservationRepository;

@Service
public class ReservationService {

    private ReservationRepository reservationRepository;

    private CDService cdService;

    public ReservationService(ReservationRepository reservationRepository, CDService cdService) {
        this.reservationRepository = reservationRepository;
        this.cdService = cdService;
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Reservation> findByUser(User user) {
        return reservationRepository.findByUser(user);
    }

    @Transactional(readOnly = true)
    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Reservation> findByUserAndStateNot(User user, ReservationState state) {
        return reservationRepository.findByUserAndStateNot(user, state);
    }

    @Transactional(readOnly = true)
    public List<Reservation> findByUserAndStateNotWithItemsAndCds(User user, ReservationState state) {
        return reservationRepository.findByUserAndStateNotWithItemsAndCds(user, state);
    }

    @Transactional
    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Transactional
    public void deleteById(Long id) {
        Optional<Reservation> optional = findById(id);
        if (optional.isPresent()) {
            Reservation reservation = optional.get();
            if (reservation.getState() != ReservationState.CANCELLED
                    && reservation.getState() != ReservationState.CART) {
                restoreStock(reservation);
            }
            reservationRepository.deleteById(id);
        }
    }

    @Transactional(readOnly = true)
    public Optional<Reservation> findByUserAndState(User user, ReservationState state) {
        return reservationRepository.findByUserAndState(user, state);
    }

    @Transactional
    public Reservation getCart(User user) {
        Optional<Reservation> cartOpt = findByUserAndState(user, ReservationState.CART);
        if (cartOpt.isPresent()) {
            return cartOpt.get();
        }
        Reservation cart = new Reservation();
        cart.setUser(user);
        cart.setState(ReservationState.CART);
        cart.setDate(java.time.LocalDate.now());
        cart.setItems(new java.util.ArrayList<>());
        return reservationRepository.save(cart);
    }

    @Transactional
    public boolean checkoutCart(Reservation cart) {
        if (cart.getItems() != null && !cart.getItems().isEmpty()) {
            for (ReservationItem item : cart.getItems()) {
                CD cd = item.getCd();
                if (cd != null && cd.getAvailableQuantity() < item.getQuantity()) {
                    return false;
                }
            }
            for (ReservationItem item : cart.getItems()) {
                CD cd = item.getCd();
                if (cd != null) {
                    cd.setAvailableQuantity(cd.getAvailableQuantity() - item.getQuantity());
                    cdService.save(cd);
                }
            }
            cart.setState(ReservationState.PENDING);
            cart.setDate(java.time.LocalDate.now());
            reservationRepository.save(cart);
            return true;
        }
        return false;
    }

    @Transactional
    public void cancelReservation(Reservation reservation) {
        if (reservation.getState() == ReservationState.PENDING) {
            reservation.setState(ReservationState.CANCELLED);
            restoreStock(reservation);
            reservationRepository.save(reservation);
        }
    }

    @Transactional
    public void updateReservationState(Reservation reservation, ReservationState newState) {
        ReservationState oldState = reservation.getState();
        if (oldState != ReservationState.CANCELLED
                && newState == ReservationState.CANCELLED) {
            restoreStock(reservation);
        } else if (oldState == ReservationState.CANCELLED
                && newState != ReservationState.CANCELLED) {
            deductStock(reservation);
        }
        reservation.setState(newState);
        reservationRepository.save(reservation);
    }

    private void restoreStock(Reservation reservation) {
        if (reservation.getItems() != null) {
            for (ReservationItem item : reservation.getItems()) {
                CD cd = item.getCd();
                if (cd != null) {
                    cd.setAvailableQuantity(cd.getAvailableQuantity() + item.getQuantity());
                    cdService.save(cd);
                }
            }
        }
    }

    private void deductStock(Reservation reservation) {
        if (reservation.getItems() != null) {
            for (ReservationItem item : reservation.getItems()) {
                CD cd = item.getCd();
                if (cd != null) {
                    cd.setAvailableQuantity(cd.getAvailableQuantity() - item.getQuantity());
                    cdService.save(cd);
                }
            }
        }
    }
}