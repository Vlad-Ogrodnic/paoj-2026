package com.pao.proiect.elearning.model;

public class CursCuProfesor {
    private final int cursId;
    private final String titluCurs;
    private final String numeProfesor;

    public CursCuProfesor(int cursId, String titluCurs, String numeProfesor) {
        this.cursId = cursId;
        this.titluCurs = titluCurs;
        this.numeProfesor = numeProfesor;
    }

    public int getCursId() {
        return cursId;
    }

    public String getTitluCurs() {
        return titluCurs;
    }

    public String getNumeProfesor() {
        return numeProfesor;
    }

    @Override
    public String toString() {
        return titluCurs + " (prof. " + numeProfesor + ")";
    }
}
