package com.pao.proiect.elearning.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Curs {
    private int id;
    private String titlu;
    private int profesorId;
    private List<Modul> module;

    public Curs(int id, String titlu, int profesorId) {
        this.id = id;
        this.titlu = titlu;
        this.profesorId = profesorId;
        this.module = new ArrayList<>();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitlu() { return titlu; }
    public void setTitlu(String titlu) { this.titlu = titlu; }
    public int getProfesorId() { return profesorId; }
    public void setProfesorId(int profesorId) { this.profesorId = profesorId; }
    
    public List<Modul> getModule() { return module; }
    
    public void adaugaModul(Modul modul) {
        this.module.add(modul);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Curs curs = (Curs) o;
        return id == curs.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Curs{" + "id=" + id + ", titlu='" + titlu + '\'' + ", profesorId=" + profesorId + '}';
    }
}
