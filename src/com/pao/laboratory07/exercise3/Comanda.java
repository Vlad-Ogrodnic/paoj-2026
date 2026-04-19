package com.pao.laboratory07.exercise3;

import com.pao.laboratory07.exercise1.OrderState;

public abstract sealed class Comanda permits ComandaStandard, ComandaRedusa, ComandaGratuita {
    protected String nume;
    protected String client;
    protected OrderState stare = OrderState.PLACED;

    public abstract double pretFinal();
    public abstract String descriere();
    public abstract String descriereScurta();

    public String getClient() {
        return client;
    }
}
