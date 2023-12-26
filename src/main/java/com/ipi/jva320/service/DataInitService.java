package com.ipi.jva320.service;

import com.ipi.jva320.model.SalarieAideADomicile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Ajoute des données de test si vide au démarrage
 */
@Component
public class DataInitService implements CommandLineRunner {

    @Autowired
    private SalarieAideADomicileService salarieAideADomicileService;

    @Override
    public void run(String... argv) throws Exception {
        for (int i=0; i<100; i++) {
            this.salarieAideADomicileService.creerSalarieAideADomicile(
                    new SalarieAideADomicile("test"+i, LocalDate.parse("2022-12-05"), LocalDate.parse("2022-12-05"),
                            i+3, (double) i /5,
                            i+5, (double) i /2, i+4));
        }
    }
}
