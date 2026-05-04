package com.pao.laboratory09.exercise3;

import com.pao.laboratory09.exercise1.Tranzactie;

import java.util.LinkedList;
import java.util.Queue;

public class CoadaTranzactii {
    private final int capacitate = 5;
    private final Queue<Tranzactie> coada = new LinkedList<>();

    public synchronized void adauga(Tranzactie t, int atmId) throws InterruptedException {
        while (coada.size() == capacitate) {
            System.out.println("[ATM-" + atmId + "] astept loc...");
            wait();
        }
        coada.add(t);
        notifyAll();
    }

    public synchronized Tranzactie extrage() throws InterruptedException {
        while (coada.isEmpty()) {
            wait();
        }
        Tranzactie t = coada.poll();
        notifyAll();
        return t;
    }
    
    public synchronized void opresteAsteptarea() {
        notifyAll();
    }
}
