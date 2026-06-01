package com.pao.proiect.elearning.service;

import com.pao.proiect.elearning.exception.EntitateNegasitaException;
import com.pao.proiect.elearning.model.Utilizator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtilizatorService {
    //singleton
    private static UtilizatorService instance;
    private final Map<Integer, Utilizator> utilizatori;
    private UtilizatorService() {
        this.utilizatori = new HashMap<>();
    }
    public static UtilizatorService getInstance() {
        if (instance == null) {
            instance = new UtilizatorService();
        }
        return instance;
    }

    //CRUD

    public void adaugaUtilizator(Utilizator utilizator) {
        if (utilizator == null) return;
        if (utilizatori.containsKey(utilizator.getId())) {
            throw new IllegalArgumentException("Un utilizator cu ID-ul " + utilizator.getId() + " exista deja!");
        }
        utilizatori.put(utilizator.getId(), utilizator);
    }

    public Utilizator cautaUtilizator(int id) throws EntitateNegasitaException {
        Utilizator u = utilizatori.get(id);
        if (u == null) {
            throw new EntitateNegasitaException("Utilizatorul cu ID " + id + " nu exista.");
        }
        return u;
    }

    public List<Utilizator> getTotiUtilizatorii() {
        return new ArrayList<>(utilizatori.values());
    }

    public void stergeUtilizator(int id) {
        utilizatori.remove(id);
    }
}
