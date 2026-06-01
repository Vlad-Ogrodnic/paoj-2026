package com.pao.proiect.elearning.model;

import java.time.LocalDateTime;
//clasa imutabila
public final class Inscriere {
    private final int cursId;
    private final int cursantId;
    private final LocalDateTime dataInscrierii;

    public Inscriere(int cursId, int cursantId) {
        this(cursId, cursantId, LocalDateTime.now());
    }

    public Inscriere(int cursId, int cursantId, LocalDateTime dataInscrierii) {
        this.cursId = cursId;
        this.cursantId = cursantId;
        this.dataInscrierii = dataInscrierii;
    }

    public int getCursId() {
        return cursId;
    }

    public int getCursantId() {
        return cursantId;
    }

    public LocalDateTime getDataInscrierii() {
        return dataInscrierii;
    }

    @Override
    public String toString() {
        return "Inscriere{" +
                "cursId=" + cursId +
                ", cursantId=" + cursantId +
                ", dataInscrierii=" + dataInscrierii +
                '}';
    }
}
