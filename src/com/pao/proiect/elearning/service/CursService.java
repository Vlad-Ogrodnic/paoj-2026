package com.pao.proiect.elearning.service;

import com.pao.proiect.elearning.exception.AccesInterzisException;
import com.pao.proiect.elearning.exception.EntitateNegasitaException;
import com.pao.proiect.elearning.model.*;

import java.util.*;

public class CursService {
    //singleton
    private static CursService instance;

    private final Map<Integer, Curs> cursuri;
    private final List<Inscriere> inscrieri;
    private final List<Scor> scoruri;

    private CursService() {
        this.cursuri = new HashMap<>();
        this.inscrieri = new ArrayList<>();
        this.scoruri = new ArrayList<>();
    }

    public static CursService getInstance() {
        if (instance == null) {
            instance = new CursService();
        }
        return instance;
    }

    public void adaugaCurs(Curs curs) {
        if (curs == null) return;
        if (cursuri.containsKey(curs.getId())) {
            throw new IllegalArgumentException("Un curs cu ID-ul " + curs.getId() + " exista deja!");
        }
        cursuri.put(curs.getId(), curs);
    }

    public Curs cautaCurs(int id) throws EntitateNegasitaException {
        Curs c = cursuri.get(id);
        if (c == null) {
            throw new EntitateNegasitaException("Cursul cu ID " + id + " nu exista.");
        }
        return c;
    }

    public void stergeCurs(int cursId) {
        cursuri.remove(cursId);
        inscrieri.removeIf(i -> i.getCursId() == cursId);
    }

    public List<Curs> getToateCursurile() {
        return new ArrayList<>(cursuri.values());
    }

    public void adaugaModulLaCurs(int cursId, Modul modul) throws EntitateNegasitaException {
        Curs curs = cautaCurs(cursId);
        curs.adaugaModul(modul);
    }

    public void adaugaMaterialLaModul(int cursId, int modulId, Material material) throws EntitateNegasitaException {
        Curs curs = cautaCurs(cursId);


        for (Modul m : curs.getModule()) {
            if (m.getId() == modulId) {
                m.adaugaMaterial(material);
                return;
            }
        }
        throw new EntitateNegasitaException("Modulul cu ID " + modulId + " nu exista in cursul " + cursId);
    }



    public void inscrieCursant(int cursId, int cursantId) throws EntitateNegasitaException {

        Curs curs = cautaCurs(cursId); 
        UtilizatorService.getInstance().cautaUtilizator(cursantId); 

        if (curs.getProfesorId() == cursantId) {
            throw new IllegalArgumentException("Un profesor nu se poate inscrie la propriul curs!");
        }

        for (Inscriere i : inscrieri) {
            if (i.getCursId() == cursId && i.getCursantId() == cursantId) {
                throw new IllegalArgumentException("Cursantul este deja inscris la acest curs!");
            }
        }
        inscrieri.add(new Inscriere(cursId, cursantId));
    }

    public void adaugaScor(int cursantId, int materialId, double valoare) {
        scoruri.add(new Scor(cursantId, materialId, valoare));
    }

    public List<Curs> getCursuriProfesor(int profesorId) {
        List<Curs> rezultat = new ArrayList<>();
        for (Curs c : cursuri.values()) {
            if (c.getProfesorId() == profesorId) {
                rezultat.add(c);
            }
        }
        return rezultat;
    }

    public List<Utilizator> getCursantiInscrisi(int cursId, int profesorCurentId) throws EntitateNegasitaException {
        Curs curs = cautaCurs(cursId);
        if (curs.getProfesorId() != profesorCurentId) {
            throw new AccesInterzisException("Acces interzis! Doar profesorul poate vedea lista cursantilor.");
        }

        List<Utilizator> cursanti = new ArrayList<>();
        UtilizatorService userService = UtilizatorService.getInstance();

        for (Inscriere i : inscrieri) {
            if (i.getCursId() == cursId) {
                try {
                    cursanti.add(userService.cautaUtilizator(i.getCursantId()));
                } catch (EntitateNegasitaException e) {

                }
            }
        }
        return cursanti;
    }

    public double calculeazaMediaCursant(int cursId, int cursantId) throws EntitateNegasitaException {
        Curs curs = cautaCurs(cursId);
        Set<Integer> quizIds = new HashSet<>();
        for (Modul m : curs.getModule()) {
            for (Material mat : m.getMateriale()) {
                if ("QUIZ".equalsIgnoreCase(mat.getTip())) {
                    quizIds.add(mat.getId());
                }
            }
        }

        if (quizIds.isEmpty()) return 0.0;

        double suma = 0;
        int count = 0;

        for (Scor s : scoruri) {
            if (s.getCursantId() == cursantId && quizIds.contains(s.getMaterialId())) {
                suma += s.getValoare();
                count++;
            }
        }
        return count > 0 ? suma / count : 0.0;
    }
}
