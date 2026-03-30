package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class PFAColaborator extends Colaborator implements PersoanaFizica {
    private double cheltuieliLunare;

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venitBrutLunar = in.nextDouble();
        this.cheltuieliLunare = in.nextDouble();
    }

    @Override
    public void afiseaza() {
        System.out.printf("%s: %s %s, venit net anual: %.2f lei\n", tipContract(), nume, prenume, calculeazaVenitNetAnual());
    }

    @Override
    public String tipContract() {
        return "PFA";
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venitNet = (venitBrutLunar - cheltuieliLunare) * 12;
        double impozit = 0.10 * venitNet;
        double salariuMinimBrut = 4050;

        double cass;
        if (venitNet < 6 * salariuMinimBrut) {
            cass = 0.10 * (6 * salariuMinimBrut);
        } else if (venitNet <= 72 * salariuMinimBrut) {
            cass = 0.10 * venitNet;
        } else {
            cass = 0.10 * (72 * salariuMinimBrut);
        }

        double cas;
        if (venitNet < 12 * salariuMinimBrut) {
            cas = 0;
        } else if (venitNet <= 24 * salariuMinimBrut) {
            cas = 0.25 * (12 * salariuMinimBrut);
        } else {
            cas = 0.25 * (24 * salariuMinimBrut);
        }

        return venitNet - impozit - cass - cas;
    }

    @Override
    public TipColaborator getTip() {
        return TipColaborator.PFA;
    }
}