package com.pao.laboratory09.exercise3;

import com.pao.laboratory09.exercise1.Tranzactie;

public class ProcessorThread implements Runnable {
    public volatile boolean activ = true;
    private final CoadaTranzactii coada;
    private int tranzactiiProcesate = 0;

    public ProcessorThread(CoadaTranzactii coada) {
        this.coada = coada;
    }

    @Override
    public void run() {
        try {
            while (activ) {
                Tranzactie t = null;
                try {
                    t = coada.extrage();
                } catch (InterruptedException e) {
                    if (!activ) {
                        break;
                    }
                    continue;
                }
                
                if (t != null) {
                    System.out.printf("[Processor] Factura #%d - %.2f RON | %s\n", t.getId(), t.getSuma(), t.getData());
                    tranzactiiProcesate++;
                    Thread.sleep(80);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public int getTranzactiiProcesate() {
        return tranzactiiProcesate;
    }
}
