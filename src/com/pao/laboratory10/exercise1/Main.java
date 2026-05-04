package com.pao.laboratory10.exercise1;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        LinkedList<Tranzactie> coada = new LinkedList<>();
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        while (scanner.hasNext()) {
            String command = scanner.next();

            if (command.equals("ENQUEUE")) {
                int id = scanner.nextInt();
                double suma = scanner.nextDouble();
                String data = scanner.next();
                TipTranzactie tip = TipTranzactie.valueOf(scanner.next());
                coada.addLast(new Tranzactie(id, suma, data, tip));
            } else if (command.equals("DEQUEUE")) {
                if (coada.isEmpty()) {
                    System.out.println("Coada goala.");
                } else {
                    Tranzactie t = coada.removeFirst();
                    System.out.println("Procesat: " + t);
                }
            } else if (command.equals("PUSH")) {
                int id = scanner.nextInt();
                double suma = scanner.nextDouble();
                String data = scanner.next();
                TipTranzactie tip = TipTranzactie.valueOf(scanner.next());
                coada.addFirst(new Tranzactie(id, suma, data, tip));
            } else if (command.equals("POP")) {
                if (coada.isEmpty()) {
                    System.out.println("Coada goala.");
                } else {
                    Tranzactie t = coada.removeFirst();
                    System.out.println("Extras: " + t);
                }
            } else if (command.equals("REMOVE_DEBIT")) {
                int count = 0;
                Iterator<Tranzactie> itr = coada.iterator();
                while (itr.hasNext()) {
                    Tranzactie t = itr.next();
                    if (t.getTip() == TipTranzactie.DEBIT) {
                        itr.remove();
                        count++;
                    }
                }
                System.out.println("Eliminat " + count + " tranzactii DEBIT.");
            } else if (command.equals("REMOVE_BELOW")) {
                double threshold = scanner.nextDouble();
                int count = 0;
                Iterator<Tranzactie> itr = coada.iterator();
                while (itr.hasNext()) {
                    Tranzactie t = itr.next();
                    if (t.getSuma() < threshold) {
                        itr.remove();
                        count++;
                    }
                }
                System.out.printf(Locale.US, "Eliminat %d tranzactii sub %.2f RON.%n", count, threshold);
            } else if (command.equals("PRINT")) {
                for (Tranzactie t : coada) {
                    System.out.println(t);
                }
            } else if (command.equals("SIZE")) {
                System.out.println("Dimensiune coada: " + coada.size());
            }
        }
    }
}
