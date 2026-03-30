package com.pao.laboratory06.exercise3;

public class Inginer extends Angajat implements PlataOnline, Comparable<Inginer> {
    private double soldBancar;

    public Inginer(String nume, String prenume, String telefon, double salariu, double soldBancar) {
        super(nume, prenume, telefon, salariu);
        this.soldBancar = soldBancar;
    }

    @Override
    public void autentificare(String user, String parola) {
        if (user == null || user.trim().isEmpty() || parola == null || parola.trim().isEmpty()) {
            throw new IllegalArgumentException("Userul sau parola nu pot fi nule sau goale.");
        }
        System.out.println("Inginer " + nume + " autentificat cu succes.");
    }

    @Override
    public double consultareSold() {
        return soldBancar;
    }

    @Override
    public boolean efectuarePlata(double suma) {
        if (suma <= 0) {
            throw new IllegalArgumentException("Suma de plată trebuie să fie pozitivă.");
        }
        if (suma > soldBancar) {
            return false;
        }
        soldBancar -= suma;
        return true;
    }
    @Override
    public int compareTo(Inginer altInginer) {
        if (this.nume == null && altInginer.nume == null) return 0;
        if (this.nume == null) return -1;
        if (altInginer.nume == null) return 1;
        return this.nume.compareTo(altInginer.nume);
    }

    @Override
    public String toString() {
        return "Inginer{" + "nume='" + nume + '\'' + ", salariu=" + salariu + '}';
    }
}