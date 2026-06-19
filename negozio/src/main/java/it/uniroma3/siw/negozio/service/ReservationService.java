package it.uniroma3.siw.negozio.service;

import org.springframework.stereotype.Service;

import it.uniroma3.siw.negozio.model.Reservation;
import it.uniroma3.siw.negozio.repository.ReservationRepository;

@Service
public class ReservationService {

    private ReservationRepository reservationRepository;

    public Iterable<Reservation> findAll() {
        return this.reservationRepository.findAll();
    }

    public Reservation findById(Long id) {
        return this.reservationRepository.findById(id).orElse(null);
    }

    public Reservation save(Reservation reservation) {
        return this.reservationRepository.save(reservation);
    }

    public void deleteById(Long id) {
        this.reservationRepository.deleteById(id);
    }
}