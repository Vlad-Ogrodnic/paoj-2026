package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

public class CustomCollectors {

    private static class Aggregator {
        Map<String, Long> byCountry = new HashMap<>();
        Map<String, Long> byChannel = new HashMap<>();
        BigDecimal total = BigDecimal.ZERO;
        List<Transaction> transactions = new ArrayList<>();
    }

    public static Collector<Transaction, ?, Snapshot> toSnapshot(int topN) {
        return Collector.of(
            Aggregator::new,
            (agg, tx) -> {
                agg.byCountry.merge(tx.getCountry(), 1L, Long::sum);
                agg.byChannel.merge(tx.getChannel(), 1L, Long::sum);
                agg.total = agg.total.add(tx.getAmount());
                agg.transactions.add(tx);
            },
            (agg1, agg2) -> {
                agg2.byCountry.forEach((k, v) -> agg1.byCountry.merge(k, v, Long::sum));
                agg2.byChannel.forEach((k, v) -> agg1.byChannel.merge(k, v, Long::sum));
                agg1.total = agg1.total.add(agg2.total);
                agg1.transactions.addAll(agg2.transactions);
                return agg1;
            },
            agg -> {
                agg.transactions.sort(Comparator.comparing(Transaction::getAmount).reversed()
                        .thenComparing(Transaction::getId));
                List<Transaction> top = agg.transactions.stream().limit(topN).toList();
                return new Snapshot(agg.byCountry, agg.byChannel, agg.total, top);
            },
            Collector.Characteristics.UNORDERED
        );
    }
}
