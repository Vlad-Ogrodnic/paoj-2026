package com.pao.laboratory07.exercise2;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) return;
        int n = Integer.parseInt(sc.nextLine().trim());
        List<Comanda> comenzi = new ArrayList<>();

        int nrStandard = 0, nrDiscounted = 0, nrGift = 0;
        double sumaStandard = 0, sumaDiscounted = 0;

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
                Comanda c = new ComandaStandard(nume, pret);
                comenzi.add(c);
                nrStandard++;
                sumaStandard += c.pretFinal();
            } else if (type.equals("DISCOUNTED")) {
                String nume = tokens[1];
                double pret = Double.parseDouble(tokens[2]);
                int discount = Integer.parseInt(tokens[3]);
                Comanda c = new ComandaRedusa(nume, pret, discount);
                comenzi.add(c);
                nrDiscounted++;
                sumaDiscounted += c.pretFinal();
            } else if (type.equals("GIFT")) {
                String nume = tokens[1];
                Comanda c = new ComandaGratuita(nume);
                comenzi.add(c);
                nrGift++;
            }
        }

        for (Comanda c : comenzi) {
            System.out.println(c.descriere());
        }

        System.out.println();
        System.out.println("Statistici:");
        if (nrStandard > 0) {
            System.out.printf(Locale.US, "STANDARD: suma = %.2f lei, numar = %d\n", sumaStandard, nrStandard);
        }
        if (nrDiscounted > 0) {
            System.out.printf(Locale.US, "DISCOUNTED: suma = %.2f lei, numar = %d\n", sumaDiscounted, nrDiscounted);
        }
        if (nrGift > 0) {
            System.out.printf(Locale.US, "GIFT: suma = 0.00 lei, numar = %d\n", nrGift);
        }
        System.out.printf(Locale.US, "Total platit: %.2f lei\n", sumaStandard + sumaDiscounted);
    }
}
