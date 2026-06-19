package it.uniroma3.siw.negozio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import it.uniroma3.siw.negozio.model.ReservationItem;
import it.uniroma3.siw.negozio.repository.ReservationItemRepository;

@Service
public class ReservationItemService {

    private ReservationItemRepository reservationItemRepository;

    public ReservationItemService(ReservationItemRepository reservationItemRepository) {
        this.reservationItemRepository = reservationItemRepository;
    }

    public List<ReservationItem> findAll() {
        return reservationItemRepository.findAll();
    }

    public Optional<ReservationItem> findById(Long id) {
        return reservationItemRepository.findById(id);
    }

    public ReservationItem save(ReservationItem reservationItem) {
        return reservationItemRepository.save(reservationItem);
    }

    public void deleteById(Long id) {
        reservationItemRepository.deleteById(id);
    }
}
