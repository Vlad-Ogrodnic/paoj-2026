package com.pao.proiect.elearning.model;


public class Cursant extends Utilizator {
    private int anStudiu;

    public Cursant(int id, String nume, String email, int anStudiu) {
        super(id, nume, email);
        this.anStudiu = anStudiu;
    }

    @Override
    public String getRol() {
        return "CURSANT";
    }

    public int getAnStudiu() {
        return anStudiu;
    }

    public void setAnStudiu(int anStudiu) {
        this.anStudiu = anStudiu;
    }

    @Override
    public String toString() {
        return "Cursant{" +
                "id=" + getId() +
                ", nume='" + getNume() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", anStudiu=" + anStudiu +
                '}';
    }
}
