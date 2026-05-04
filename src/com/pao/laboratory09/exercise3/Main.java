package com.pao.laboratory09.exercise3;

public class Main {
    public static void main(String[] args) {
        CoadaTranzactii coada = new CoadaTranzactii();

        ATMThread atm1 = new ATMThread(1, coada);
        ATMThread atm2 = new ATMThread(2, coada);
        ATMThread atm3 = new ATMThread(3, coada);

        ProcessorThread processorRunnable = new ProcessorThread(coada);
        Thread processorThread = new Thread(processorRunnable);

        atm1.start();
        atm2.start();
        atm3.start();
        processorThread.start();

        try {
            atm1.join();
            atm2.join();
            atm3.join();
            
            Thread.sleep(500); 

            processorRunnable.activ = false;
            coada.opresteAsteptarea();
            processorThread.interrupt();
            
            processorThread.join();
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Toate tranzactiile procesate. Total: " + processorRunnable.getTranzactiiProcesate());
    }
}
