package it.uniroma3.siw.negozio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import it.uniroma3.siw.negozio.model.CD;
import it.uniroma3.siw.negozio.repository.CDRepository;

@Service
public class CDService {

    private CDRepository cdRepository;

    public CDService(CDRepository cdRepository) {
        this.cdRepository = cdRepository;
    }

    public List<CD> findAll() {
        return cdRepository.findAll();
    }

    public Optional<CD> findById(Long id) {
        return cdRepository.findById(id);
    }

    public CD save(CD cd) {
        return cdRepository.save(cd);
    }

    public void deleteById(Long id) {
        cdRepository.deleteById(id);
    }
}