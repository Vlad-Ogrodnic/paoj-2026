package com.pao.proiect.elearning.service;

import com.pao.proiect.elearning.exception.AccesInterzisException;
import com.pao.proiect.elearning.exception.EntitateNegasitaException;
import com.pao.proiect.elearning.model.*;
import com.pao.proiect.elearning.repository.*;

import java.sql.SQLException;
import java.util.List;

public class CursService {
    private static CursService instance;

    private final CursRepository cursRepository;
    private final ModulRepository modulRepository;
    private final MaterialRepository materialRepository;
    private final InscriereRepository inscriereRepository;
    private final ScorRepository scorRepository;

    private CursService() {
        this.cursRepository = new CursRepository();
        this.modulRepository = new ModulRepository();
        this.materialRepository = new MaterialRepository();
        this.inscriereRepository = new InscriereRepository();
        this.scorRepository = new ScorRepository();
    }

    public static CursService getInstance() {
        if (instance == null) {
            instance = new CursService();
        }
        return instance;
    }

    public void adaugaCurs(Curs curs) {
        if (curs == null) return;
        if (cursRepository.findById(curs.getId()).isPresent()) {
            throw new IllegalArgumentException("Un curs cu ID-ul " + curs.getId() + " exista deja!");
        }
        cursRepository.save(curs);
    }

    public Curs cautaCurs(int id) throws EntitateNegasitaException {
        return cursRepository.findById(id)
                .orElseThrow(() -> new EntitateNegasitaException("Cursul cu ID " + id + " nu exista."));
    }

    public void stergeCurs(int cursId) {
        try {
            cursRepository.deleteCuDependinte(cursId);
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea cursului si a dependentelor", e);
        }
    }

    public List<Curs> getToateCursurile() {
        return cursRepository.findAll();
    }

    public void adaugaModulLaCurs(int cursId, Modul modul) throws EntitateNegasitaException {
        cautaCurs(cursId);
        if (modulRepository.findById(modul.getId()).isPresent()) {
            throw new IllegalArgumentException("Un modul cu ID-ul " + modul.getId() + " exista deja!");
        }
        modulRepository.save(cursId, modul);
    }

    public void adaugaMaterialLaModul(int cursId, int modulId, Material material) throws EntitateNegasitaException {
        Curs curs = cautaCurs(cursId);

        boolean modulExista = false;
        for (Modul m : curs.getModule()) {
            if (m.getId() == modulId) {
                modulExista = true;
                break;
            }
        }
        if (!modulExista) {
            throw new EntitateNegasitaException("Modulul cu ID " + modulId + " nu exista in cursul " + cursId);
        }
        if (materialRepository.findById(material.getId()).isPresent()) {
            throw new IllegalArgumentException("Un material cu ID-ul " + material.getId() + " exista deja!");
        }
        materialRepository.save(modulId, material);
    }

    public void inscrieCursant(int cursId, int cursantId) throws EntitateNegasitaException {
        Curs curs = cautaCurs(cursId);
        UtilizatorService.getInstance().cautaUtilizator(cursantId);

        if (curs.getProfesorId() == cursantId) {
            throw new IllegalArgumentException("Un profesor nu se poate inscrie la propriul curs!");
        }
        if (inscriereRepository.exists(cursId, cursantId)) {
            throw new IllegalArgumentException("Cursantul este deja inscris la acest curs!");
        }
        inscriereRepository.save(new Inscriere(cursId, cursantId));
    }

    public void adaugaScor(int cursantId, int materialId, double valoare) {
        Scor scor = new Scor(cursantId, materialId, valoare);
        if (scorRepository.findByCursantAndMaterial(cursantId, materialId).isPresent()) {
            scorRepository.update(scor);
        } else {
            scorRepository.save(scor);
        }
    }

    public List<Curs> getCursuriProfesor(int profesorId) {
        return cursRepository.findByProfesorId(profesorId);
    }

    public List<CursCuProfesor> getCursuriProfesorCuDetalii(int profesorId) {
        return cursRepository.findCursuriCuProfesorJoin(profesorId);
    }

    public List<Utilizator> getCursantiInscrisi(int cursId, int profesorCurentId) throws EntitateNegasitaException {
        Curs curs = cautaCurs(cursId);
        if (curs.getProfesorId() != profesorCurentId) {
            throw new AccesInterzisException("Acces interzis! Doar profesorul poate vedea lista cursantilor.");
        }
        return inscriereRepository.findCursantiInscrisiCuJoin(cursId);
    }

    public double calculeazaMediaCursant(int cursId, int cursantId) throws EntitateNegasitaException {
        cautaCurs(cursId);
        UtilizatorService.getInstance().cautaUtilizator(cursantId);
        return scorRepository.calculeazaMediaLaCursCuJoin(cursId, cursantId);
    }
}
