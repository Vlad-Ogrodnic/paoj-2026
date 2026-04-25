package com.pao.proiect.elearning.model;

public class Scor {
    private int cursantId;
    private int materialId; // quiz
    private double valoare;

    public Scor(int cursantId, int materialId, double valoare) {
        this.cursantId = cursantId;
        this.materialId = materialId;
        this.valoare = valoare;
    }

    public int getCursantId() { return cursantId; }
    public void setCursantId(int cursantId) { this.cursantId = cursantId; }
    public int getMaterialId() { return materialId; }
    public void setMaterialId(int materialId) { this.materialId = materialId; }
    public double getValoare() { return valoare; }
    public void setValoare(double valoare) { this.valoare = valoare; }

    @Override
    public String toString() {
        return "Scor{" + "cursantId=" + cursantId + ", materialId=" + materialId + ", valoare=" + valoare + '}';
    }
}
