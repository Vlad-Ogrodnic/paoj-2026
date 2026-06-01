package com.pao.proiect.elearning.model;

import java.util.Objects;
//clasa abstracta
public abstract class Utilizator {
    private int id;
    private String nume;
    private String email;

    public Utilizator(int id, String nume, String email) {
        this.id = id;
        this.nume = nume;
        this.email = email;
    }
    public abstract String getRol();
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNume() {
        return nume;
    }
    public void setNume(String nume) {
        this.nume = nume;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    @Override
    public String toString() {
        return "Utilizator{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilizator that = (Utilizator) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
