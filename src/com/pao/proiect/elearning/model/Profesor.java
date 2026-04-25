package com.pao.proiect.elearning.model;

public class Profesor extends Utilizator {
    private String departament;

    public Profesor(int id, String nume, String email, String departament) {
        super(id, nume, email);
        this.departament = departament;
    }

    @Override
    public String getRol() {
        return "PROFESOR";
    }

    public String getDepartament() {
        return departament;
    }

    public void setDepartament(String departament) {
        this.departament = departament;
    }

    @Override
    public String toString() {
        return "Profesor{" +
                "id=" + getId() +
                ", nume='" + getNume() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", departament='" + departament + '\'' +
                '}';
    }
}
