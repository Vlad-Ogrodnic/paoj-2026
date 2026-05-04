package com.pao.laboratory10.exercise2;

import com.pao.laboratory10.exercise1.Tranzactie;
import com.pao.laboratory10.exercise1.TipTranzactie;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        if (!scanner.hasNextInt()) return;

        int n = scanner.nextInt();
        ArrayList<Tranzactie> lista = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int id = scanner.nextInt();
            double suma = scanner.nextDouble();
            String data = scanner.next();
            TipTranzactie tip = TipTranzactie.valueOf(scanner.next());
            lista.add(new Tranzactie(id, suma, data, tip));
        }

        while (scanner.hasNext()) {
            String command = scanner.next();

            if (command.equals("UNIQUE_IDS")) {
                LinkedHashSet<Integer> uniqueIds = new LinkedHashSet<>();
                for (Tranzactie t : lista) {
                    uniqueIds.add(t.getId());
                }
                System.out.println("IDs unice (" + uniqueIds.size() + "): " + uniqueIds.toString());
            } else if (command.equals("MONTHLY_REPORT")) {
                TreeMap<String, double[]> report = new TreeMap<>();
                for (Tranzactie t : lista) {
                    String month = t.getData().substring(0, 7);
                    report.putIfAbsent(month, new double[2]);
                    if (t.getTip() == TipTranzactie.CREDIT) {
                        report.get(month)[0] += t.getSuma();
                    } else if (t.getTip() == TipTranzactie.DEBIT) {
                        report.get(month)[1] += t.getSuma();
                    }
                }
                for (Map.Entry<String, double[]> entry : report.entrySet()) {
                    System.out.printf(Locale.US, "%s: CREDIT %.2f RON, DEBIT %.2f RON%n",
                            entry.getKey(), entry.getValue()[0], entry.getValue()[1]);
                }
            } else if (command.equals("TOP")) {
                int topN = scanner.nextInt();
                ArrayList<Tranzactie> copy = new ArrayList<>(lista);
                copy.sort((t1, t2) -> Double.compare(t2.getSuma(), t1.getSuma()));
                System.out.println("Top " + topN + ":");
                int limit = Math.min(topN, copy.size());
                for (int i = 0; i < limit; i++) {
                    System.out.println(copy.get(i));
                }
            } else if (command.equals("SORT_ASC")) {
                lista.sort(Comparator.comparingDouble(Tranzactie::getSuma));
                for (Tranzactie t : lista) {
                    System.out.println(t);
                }
            } else if (command.equals("SORT_DESC")) {
                lista.sort((t1, t2) -> Double.compare(t2.getSuma(), t1.getSuma()));
                for (Tranzactie t : lista) {
                    System.out.println(t);
                }
            } else if (command.equals("REVERSE")) {
                Collections.reverse(lista);
                for (Tranzactie t : lista) {
                    System.out.println(t);
                }
            } else if (command.equals("MIN_MAX")) {
                if (!lista.isEmpty()) {
                    Tranzactie minT = Collections.min(lista, Comparator.comparingDouble(Tranzactie::getSuma));
                    Tranzactie maxT = Collections.max(lista, Comparator.comparingDouble(Tranzactie::getSuma));
                    System.out.println("MIN: " + minT);
                    System.out.println("MAX: " + maxT);
                }
            } else if (command.equals("CME_DEMO")) {
                try {
                    for (Tranzactie t : lista) {
                        lista.remove(t);
                    }
                } catch (ConcurrentModificationException e) {
                    System.out.println("ConcurrentModificationException prins: modificare in iteratie detectata.");
                }
            }
        }
    }
}
