package com.pao.laboratory06.exercise3;

import java.util.ArrayList;
import java.util.List;

public class PersoanaJuridica extends Persoana implements PlataOnlineSMS {
    private double soldBancar;
    private List<String> smsTrimise;

    public PersoanaJuridica(String nume, String prenume, String telefon, double soldBancar) {
        super(nume, prenume, telefon);
        this.soldBancar = soldBancar;
        this.smsTrimise = new ArrayList<>();
    }

    @Override
    public void autentificare(String user, String parola) {
        if (user == null || user.trim().isEmpty() || parola == null || parola.trim().isEmpty()) {
            throw new IllegalArgumentException("Userul sau parola invalide pentru persoana juridică.");
        }
        System.out.println("Persoana juridica " + nume + " s-a autentificat.");
    }

    @Override
    public double consultareSold() {
        return soldBancar;
    }

    @Override
    public boolean efectuarePlata(double suma) {
        if (suma <= 0) throw new IllegalArgumentException("Suma invalidă.");
        if (suma > soldBancar) return false;
        soldBancar -= suma;
        return true;
    }

    @Override
    public boolean trimiteSMS(String mesaj) {
        if (telefon == null || telefon.trim().isEmpty()) {
            System.out.println("[Eroare SMS] " + nume + " nu are număr de telefon asociat.");
            return false;
        }
        if (mesaj == null || mesaj.trim().isEmpty()) {
            System.out.println("[Eroare SMS] Nu se poate trimite un mesaj gol.");
            return false;
        }
        smsTrimise.add(mesaj);
        System.out.println("[SMS Trimis -> " + telefon + "]: " + mesaj);
        return true;
    }

    public List<String> getSmsTrimise() {
        return smsTrimise;
    }
}