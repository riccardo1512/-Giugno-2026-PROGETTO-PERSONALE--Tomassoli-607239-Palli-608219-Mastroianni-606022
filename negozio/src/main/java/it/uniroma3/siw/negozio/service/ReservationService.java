package it.uniroma3.siw.negozio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.negozio.model.Reservation;
import it.uniroma3.siw.negozio.model.User;
import it.uniroma3.siw.negozio.repository.ReservationRepository;

@Service
public class ReservationService {

    private ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
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
    public List<Reservation> findByUserAndStateNot(User user, it.uniroma3.siw.negozio.model.ReservationState state) {
        return reservationRepository.findByUserAndStateNot(user, state);
    }

    @Transactional
    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Reservation> findByUserAndState(User user, it.uniroma3.siw.negozio.model.ReservationState state) {
        return reservationRepository.findByUserAndState(user, state);
    }

    @Transactional
    public Reservation getCart(User user) {
        Optional<Reservation> cartOpt = findByUserAndState(user, it.uniroma3.siw.negozio.model.ReservationState.CART);
        if (cartOpt.isPresent()) {
            return cartOpt.get();
        }
        Reservation cart = new Reservation();
        cart.setUser(user);
        cart.setState(it.uniroma3.siw.negozio.model.ReservationState.CART);
        cart.setDate(java.time.LocalDate.now());
        cart.setItems(new java.util.ArrayList<>());
        return reservationRepository.save(cart);
    }
}