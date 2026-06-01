package com.pao.proiect.elearning.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Modul {
    private int id;
    private String titlu;
    private List<Material> materiale; // Un tip de colectie (List)

    public Modul(int id, String titlu) {
        this.id = id;
        this.titlu = titlu;
        this.materiale = new ArrayList<>(); // Initializam lista goala
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitlu() { return titlu; }
    public void setTitlu(String titlu) { this.titlu = titlu; }
    
    public List<Material> getMateriale() { 
        return materiale; 
    }
    
    public void adaugaMaterial(Material material) {
        this.materiale.add(material);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Modul modul = (Modul) o;
        return id == modul.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Modul{" + "id=" + id + ", titlu='" + titlu + '\'' + ", materiale=" + materiale.size() + '}';
    }
}
