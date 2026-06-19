package it.uniroma3.siw.negozio.service;

import org.springframework.stereotype.Service;

import it.uniroma3.siw.negozio.model.ReservationItem;
import it.uniroma3.siw.negozio.repository.ReservationItemRepository;

@Service
public class ReservationItemService {

    private ReservationItemRepository reservationItemRepository;

    public Iterable<ReservationItem> findAll() {
        return this.reservationItemRepository.findAll();
    }

    public ReservationItem findById(Long id) {
        return this.reservationItemRepository.findById(id).orElse(null);
    }

    public ReservationItem save(ReservationItem reservationItem) {
        return this.reservationItemRepository.save(reservationItem);
    }

    public void deleteById(Long id) {
        this.reservationItemRepository.deleteById(id);
    }
}
