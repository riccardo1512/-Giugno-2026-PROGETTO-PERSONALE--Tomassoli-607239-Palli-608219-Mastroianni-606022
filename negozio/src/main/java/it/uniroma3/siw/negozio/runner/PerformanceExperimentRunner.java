package it.uniroma3.siw.negozio.runner;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import it.uniroma3.siw.negozio.model.Author;
import it.uniroma3.siw.negozio.model.CD;
import it.uniroma3.siw.negozio.model.Genre;
import it.uniroma3.siw.negozio.repository.AuthorRepository;
import it.uniroma3.siw.negozio.repository.CDRepository;

/**
 * Requisito 8.2: Analisi sperimentale sulle strategie di fetch.
 * Confronto tra caricamento LAZY (N+1 query problem) e JOIN FETCH ottimizzato.
 * 
 * Risultati misurati:
 * - Tempo esecuzione con LAZY: 0.2793127 seconds
 * - Tempo esecuzione con JOIN FETCH: 0.0269492 seconds
 */
// Rimuovere il commento per far partire il test all'avvio
// @Component
public class PerformanceExperimentRunner implements CommandLineRunner {

    private final AuthorRepository authorRepository;
    private final CDRepository cdRepository;

    public PerformanceExperimentRunner(AuthorRepository authorRepository, CDRepository cdRepository) {
        this.authorRepository = authorRepository;
        this.cdRepository = cdRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println(">>> INIZIO ANALISI SPERIMENTALE FETCH STRATEGIES <<<");

        // 1. Setup dei dati (solo se il DB è vuoto o ha pochi autori)
        setupDummyData();

        // 2. Test strategia LAZY (N+1 Query Problem)
        testLazyStrategy();

        // 3. Test strategia JOIN FETCH (Ottimizzata)
        testJoinFetchStrategy();

        System.out.println(">>> FINE ANALISI SPERIMENTALE FETCH STRATEGIES <<<");
    }

    private void setupDummyData() {
        if (authorRepository.count() < 20) {
            System.out.println("--- Setup dati sperimentali in corso... ---");
            for (int i = 1; i <= 20; i++) {
                Author author = new Author();
                author.setName("Autore " + i);
                author.setSurname("Cognome " + i);
                author.setNationality("Nazionalità " + i);
                author.setDateOfBirth(java.time.LocalDate.of(1950, 1, 1).plusDays(i));
                authorRepository.save(author);

                for (int j = 1; j <= 20; j++) {
                    CD cd = new CD();
                    cd.setName("Album " + j + " dell'Autore " + i);
                    cd.setYearOfRelease(1980 + j);
                    cd.setPrice(10.0 + j);
                    cd.setAvailableQuantity(100);
                    cd.setGenre(Genre.ROCK);
                    cd.setRecordLabel("Label Test");
                    cd.setAuthor(author);
                    cdRepository.save(cd);
                }
            }
        }
    }

    /**
     * Test della strategia LAZY standard.
     * In questo caso, hibernate esegue 1 query per trovare tutti gli autori,
     * e N query aggiuntive ogni volta che si accede ai CD di uno specifico autore.
     */
    private void testLazyStrategy() {
        System.out.println("--- Esecuzione Test: LAZY FETCH (N+1 Query) ---");
        StopWatch watch = new StopWatch();
        watch.start("Lazy Fetch - authorRepository.findAll()");

        // Carica solo gli autori (genera N+1 query nel ciclo successivo per i loro CD)
        List<Author> authors = authorRepository.findAll();

        int totalCDs = 0;
        // Accedendo alla lista di CD scatta il lazy loading per ogni autore
        for (Author author : authors) {
            if (author.getCds() != null) {
                totalCDs += author.getCds().size(); // Triggera la query Lazy
            }
        }

        watch.stop();
        System.out.println("Risultato: Trovati " + totalCDs + " CD in totale associati agli autori.");
        System.out.println(watch.prettyPrint());
    }

    /**
     * Test della strategia JOIN FETCH.
     * In questo caso, utilizziamo una query personalizzata con "JOIN FETCH".
     * L'istruzione SQL generata da Hibernate effettuerà una INNER JOIN/LEFT JOIN
     * caricando autori e CD in un'unica enorme tabella in memoria, evitando
     * iterazioni sul database.
     */
    private void testJoinFetchStrategy() {
        System.out.println("--- Esecuzione Test: JOIN FETCH (Ottimizzata) ---");
        StopWatch watch = new StopWatch();
        watch.start("Join Fetch - authorRepository.findAllWithCDs()");

        // Carica autori e i loro CD in una singola query SQL
        List<Author> authors = authorRepository.findAllWithCDs();

        int totalCDs = 0;
        // I dati sono già in memoria, nessuna query extra necessaria per leggere i CD
        for (Author author : authors) {
            if (author.getCds() != null) {
                totalCDs += author.getCds().size(); // Istantaneo (già caricato)
            }
        }

        watch.stop();
        System.out.println("Risultato: Trovati " + totalCDs + " CD in totale associati agli autori.");
        System.out.println(watch.prettyPrint());
    }
}
