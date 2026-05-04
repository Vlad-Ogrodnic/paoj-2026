package com.pao.laboratory09.exercise3;

import com.pao.laboratory09.exercise1.TipTranzactie;
import com.pao.laboratory09.exercise1.Tranzactie;

import java.util.concurrent.atomic.AtomicInteger;

public class ATMThread extends Thread {
    private final int id;
    private final CoadaTranzactii coada;
    private static final AtomicInteger tranzactieIdGenerator = new AtomicInteger(1);

    public ATMThread(int id, CoadaTranzactii coada) {
        this.id = id;
        this.coada = coada;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 4; i++) {
                int tId = tranzactieIdGenerator.getAndIncrement();
                double suma = 100.0 + (Math.random() * 900.0);
                Tranzactie t = new Tranzactie(tId, suma, "2024-05-04", "RO01ATM" + id, "RO99BNK", TipTranzactie.CREDIT);
                
                System.out.printf("[ATM-%d] trimite: Tranzactie #%d %.2f RON\n", id, t.getId(), t.getSuma());
                coada.adauga(t, id);
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
