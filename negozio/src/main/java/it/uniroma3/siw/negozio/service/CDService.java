package it.uniroma3.siw.negozio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.negozio.model.CD;
import it.uniroma3.siw.negozio.repository.CDRepository;

@Service
public class CDService {

    private final CDRepository cdRepository;
    private final it.uniroma3.siw.negozio.repository.ReservationItemRepository reservationItemRepository;

    public CDService(CDRepository cdRepository, it.uniroma3.siw.negozio.repository.ReservationItemRepository reservationItemRepository) {
        this.cdRepository = cdRepository;
        this.reservationItemRepository = reservationItemRepository;
    }

    @Transactional(readOnly = true)
    public List<CD> findAll() {
        return cdRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<CD> findById(Long id) {
        return cdRepository.findById(id);
    }

    @Transactional
    public CD save(CD cd) {
        return cdRepository.save(cd);
    }

    @Transactional
    public void deleteById(Long id) {
        Optional<CD> cdOpt = cdRepository.findById(id);
        if (cdOpt.isPresent()) {
            CD cd = cdOpt.get();
            List<it.uniroma3.siw.negozio.model.ReservationItem> items = reservationItemRepository.findByCd(cd);
            reservationItemRepository.deleteAll(items);
            cdRepository.delete(cd);
        }
    }
}