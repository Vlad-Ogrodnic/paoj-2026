package com.pao.proiect.elearning.model;

import java.util.Objects;

public class Material {
    private int id;
    private String titlu;
    private String tip; // ex: "VIDEO", "PDF", "QUIZ"

    public Material(int id, String titlu, String tip) {
        this.id = id;
        this.titlu = titlu;
        this.tip = tip;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitlu() { return titlu; }
    public void setTitlu(String titlu) { this.titlu = titlu; }
    public String getTip() { return tip; }
    public void setTip(String tip) { this.tip = tip; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Material material = (Material) o;
        return id == material.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Material{" + "id=" + id + ", titlu='" + titlu + '\'' + ", tip='" + tip + '\'' + '}';
    }
}
