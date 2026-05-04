package com.pao.laboratory10.exercise3;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Tranzactie> tranzactii = Arrays.asList(
            new Tranzactie(1, 1500.00, "2024-01-10", TipTranzactie.CREDIT, "CONT_A"),
            new Tranzactie(2, 200.00, "2024-01-15", TipTranzactie.DEBIT, "CONT_B"),
            new Tranzactie(3, 50.00, "2024-01-20", TipTranzactie.DEBIT, "CONT_A"),
            new Tranzactie(4, 3000.00, "2024-02-01", TipTranzactie.CREDIT, "CONT_C"),
            new Tranzactie(5, 150.00, "2024-02-14", TipTranzactie.DEBIT, "CONT_D"),
            new Tranzactie(6, 400.00, "2024-02-28", TipTranzactie.DEBIT, "CONT_B"),
            new Tranzactie(7, 2000.00, "2024-03-05", TipTranzactie.CREDIT, "CONT_E"),
            new Tranzactie(8, 10000.00, "2024-03-10", TipTranzactie.DEBIT, "CONT_A"),
            new Tranzactie(9, 800.00, "2024-03-15", TipTranzactie.CREDIT, "CONT_C"),
            new Tranzactie(10, 60.00, "2024-03-20", TipTranzactie.DEBIT, "CONT_E")
        );

        System.out.println("--- 1. Lista tuturor tranzacțiilor CREDIT ---");
        tranzactii.stream()
                .filter(t -> t.getTip() == TipTranzactie.CREDIT)
                .forEach(System.out::println);
        System.out.println();

        System.out.println("--- 2. Total procesat ---");
        double totalProcesat = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .sum();
        System.out.printf(Locale.US, "Total procesat: %.2f RON%n", totalProcesat);
        System.out.println();

        System.out.println("--- 3. Per luna ---");
        Map<String, Double> totalPerLuna = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0, 7),
                        TreeMap::new,
                        Collectors.summingDouble(Tranzactie::getSuma)
                ));
        totalPerLuna.forEach((luna, suma) -> System.out.printf(Locale.US, "%s: %.2f RON%n", luna, suma));
        System.out.println();

        System.out.println("--- 4. Top 3 tranzactii ---");
        System.out.println("Top 3 tranzactii:");
        tranzactii.stream()
                .sorted(Comparator.comparingDouble(Tranzactie::getSuma).reversed())
                .limit(3)
                .forEach(System.out::println);
        System.out.println();

        System.out.println("--- 5. Conturi sursa unice ---");
        List<String> conturiUnice = tranzactii.stream()
                .map(Tranzactie::getContSursa)
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Conturi sursa unice: " + conturiUnice);
        System.out.println();

        System.out.println("--- 6. Suma medie ---");
        OptionalDouble optionalAvg = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .average();
        System.out.printf(Locale.US, "Suma medie: %.2f RON%n", optionalAvg.orElse(0.0));
        System.out.println();

        System.out.println("--- 7. EXTRAS DE CONT per lună ---");
        Map<String, List<Tranzactie>> tranzactiiPerLuna = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0, 7),
                        TreeMap::new,
                        Collectors.toList()
                ));
        tranzactiiPerLuna.forEach((luna, listaTranzactii) -> {
            double totalLuna = listaTranzactii.stream().mapToDouble(Tranzactie::getSuma).sum();
            System.out.printf(Locale.US, "EXTRAS DE CONT - %s: %d tranzactii, total: %.2f RON%n", luna, listaTranzactii.size(), totalLuna);
        });
        System.out.println();
    }
}
