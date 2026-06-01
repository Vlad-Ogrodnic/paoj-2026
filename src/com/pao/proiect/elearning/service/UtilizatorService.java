package com.pao.proiect.elearning.service;

import com.pao.proiect.elearning.exception.EntitateNegasitaException;
import com.pao.proiect.elearning.model.Utilizator;
import com.pao.proiect.elearning.repository.UtilizatorRepository;

import java.util.List;

public class UtilizatorService {
    private static UtilizatorService instance;
    private final UtilizatorRepository utilizatorRepository;

    private UtilizatorService() {
        this.utilizatorRepository = new UtilizatorRepository();
    }

    public static UtilizatorService getInstance() {
        if (instance == null) {
            instance = new UtilizatorService();
        }
        return instance;
    }

    public void adaugaUtilizator(Utilizator utilizator) {
        if (utilizator == null) return;
        if (utilizatorRepository.findById(utilizator.getId()).isPresent()) {
            throw new IllegalArgumentException("Un utilizator cu ID-ul " + utilizator.getId() + " exista deja!");
        }
        utilizatorRepository.save(utilizator);
    }

    public Utilizator cautaUtilizator(int id) throws EntitateNegasitaException {
        return utilizatorRepository.findById(id)
                .orElseThrow(() -> new EntitateNegasitaException("Utilizatorul cu ID " + id + " nu exista."));
    }

    public List<Utilizator> getTotiUtilizatorii() {
        return utilizatorRepository.findAll();
    }

    public void stergeUtilizator(int id) {
        utilizatorRepository.delete(id);
    }
}
