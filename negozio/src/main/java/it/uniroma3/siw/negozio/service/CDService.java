package it.uniroma3.siw.negozio.service;

import org.springframework.stereotype.Service;

import it.uniroma3.siw.negozio.model.CD;
import it.uniroma3.siw.negozio.repository.CDRepository;

@Service
public class CDService {

    private CDRepository cdRepository;

    public Iterable<CD> findAll() {
        return this.cdRepository.findAll();
    }

    public CD findById(Long id) {
        return this.cdRepository.findById(id).orElse(null);
    }

    public CD save(CD cd) {
        return this.cdRepository.save(cd);
    }

    public void deleteById(Long id) {
        this.cdRepository.deleteById(id);
    }
}