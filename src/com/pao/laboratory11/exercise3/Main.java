package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        List<Transaction> data = List.of(
            new Transaction(1, new BigDecimal("100.50"), LocalDate.of(2023, 1, 10), "RO", "Online"),
            new Transaction(2, new BigDecimal("250.00"), LocalDate.of(2023, 1, 11), "UK", "InStore"),
            new Transaction(3, new BigDecimal("150.00"), LocalDate.of(2023, 1, 11), "RO", "Online"),
            new Transaction(4, new BigDecimal("50.00"), LocalDate.of(2023, 1, 12), "FR", "Online"),
            new Transaction(5, new BigDecimal("500.00"), LocalDate.of(2023, 1, 13), "UK", "Online"),
            new Transaction(6, new BigDecimal("500.00"), LocalDate.of(2023, 1, 14), "RO", "InStore"),
            new Transaction(7, new BigDecimal("300.00"), LocalDate.of(2023, 1, 14), "FR", "InStore")
        );

        Snapshot snap = data.stream().collect(CustomCollectors.toSnapshot(3));

        System.out.println(snap.getTotalAmount());

        snap.getTopTransactions().forEach(System.out::println);

        snap.getCountByCountry().entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .forEach(System.out::println);

        snap.getCountByChannel().entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .forEach(System.out::println);
    }
}
