package com.pao.laboratory07.exercise3;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) return;

        int n = Integer.parseInt(sc.nextLine().trim());
        List<Comanda> comenzi = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            if (!sc.hasNextLine()) break;
            String line = sc.nextLine().trim();
            if (line.isEmpty()) {
                i--;
                continue;
            }
            String[] tokens = line.split("\\s+");
            String type = tokens[0];

            if (type.equals("STANDARD")) {
                String nume = tokens[1];
                double pret = Double.parseDouble(tokens[2]);
                String client = tokens[3];
                comenzi.add(new ComandaStandard(nume, pret, client));
            } else if (type.equals("DISCOUNTED")) {
                String nume = tokens[1];
                double pret = Double.parseDouble(tokens[2]);
                int discount = Integer.parseInt(tokens[3]);
                String client = tokens[4];
                comenzi.add(new ComandaRedusa(nume, pret, discount, client));
            } else if (type.equals("GIFT")) {
                String nume = tokens[1];
                String client = tokens[2];
                comenzi.add(new ComandaGratuita(nume, client));
            }
        }

        for (Comanda c : comenzi) {
            System.out.println(c.descriere());
        }

        while (sc.hasNextLine()) {
            String cmdLine = sc.nextLine().trim();
            if (cmdLine.isEmpty()) continue;
            String[] tokens = cmdLine.split("\\s+");
            String cmd = tokens[0];

            if (cmd.equals("STATS")) {
                System.out.println("\n--- STATS ---");
                Map<Class<? extends Comanda>, Double> averages = comenzi.stream()
                        .collect(Collectors.groupingBy(Comanda::getClass, Collectors.averagingDouble(Comanda::pretFinal)));

                if (averages.containsKey(ComandaStandard.class)) {
                    System.out.printf(Locale.US, "STANDARD: medie = %.2f lei\n", averages.get(ComandaStandard.class));
                }
                if (averages.containsKey(ComandaRedusa.class)) {
                    System.out.printf(Locale.US, "DISCOUNTED: medie = %.2f lei\n", averages.get(ComandaRedusa.class));
                }
                if (averages.containsKey(ComandaGratuita.class)) {
                    System.out.printf(Locale.US, "GIFT: medie = %.2f lei\n", averages.get(ComandaGratuita.class));
                }

            } else if (cmd.equals("FILTER")) {
                double threshold = Double.parseDouble(tokens[1]);
                System.out.printf(Locale.US, "\n--- FILTER (>= %.2f) ---\n", threshold);
                comenzi.stream()
                        .filter(c -> c.pretFinal() >= threshold)
                        .forEach(c -> System.out.println(c.descriereScurta()));

            } else if (cmd.equals("SORT")) {
                System.out.println("\n--- SORT (by client, then by pret) ---");
                comenzi.stream()
                        .sorted(Comparator.comparing(Comanda::getClient).thenComparing(Comanda::pretFinal))
                        .forEach(c -> System.out.println(c.descriereScurta()));

            } else if (cmd.equals("SPECIAL")) {
                System.out.println("\n--- SPECIAL (discount > 15%) ---");
                comenzi.stream()
                        .filter(c -> c instanceof ComandaRedusa && ((ComandaRedusa) c).getDiscountProcent() > 15)
                        .forEach(c -> System.out.println(c.descriereScurta()));

            } else if (cmd.equals("QUIT")) {
                break;
            }
        }
    }
}
