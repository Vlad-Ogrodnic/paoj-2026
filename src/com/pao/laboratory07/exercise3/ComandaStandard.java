package com.pao.laboratory07.exercise3;

import java.util.Locale;

public final class ComandaStandard extends Comanda {
    protected double pret;

    public ComandaStandard(String nume, double pret, String client) {
        this.nume = nume;
        this.pret = pret;
        this.client = client;
    }

    @Override
    public double pretFinal() {
        return pret;
    }

    @Override
    public String descriere() {
        return String.format(Locale.US, "STANDARD: %s, pret: %.2f lei [%s] - client: %s", nume, pretFinal(), stare, client);
    }

    @Override
    public String descriereScurta() {
        return String.format(Locale.US, "STANDARD: %s, pret: %.2f lei - client: %s", nume, pretFinal(), client);
    }
}
