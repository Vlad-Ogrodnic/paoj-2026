package com.pao.laboratory09.exercise1;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex1.ser";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        if (!scanner.hasNextInt()) {
            return;
        }

        int n = scanner.nextInt();
        List<Tranzactie> tranzactii = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int id = scanner.nextInt();
            double suma = scanner.nextDouble();
            String data = scanner.next();
            String contSursa = scanner.next();
            String contDestinatie = scanner.next();
            TipTranzactie tip = TipTranzactie.valueOf(scanner.next());

            Tranzactie t = new Tranzactie(id, suma, data, contSursa, contDestinatie, tip);
            t.setNote("procesat");
            tranzactii.add(t);
        }
        File outputFile = new File(OUTPUT_FILE);
        File parentDir = outputFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }


        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            oos.writeObject(tranzactii);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Tranzactie> deserializedTranzactii = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(OUTPUT_FILE))) {
            deserializedTranzactii = (List<Tranzactie>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (deserializedTranzactii == null) {
            return;
        }

        while (scanner.hasNext()) {
            String command = scanner.next();
            if ("LIST".equals(command)) {
                for (Tranzactie t : deserializedTranzactii) {
                    System.out.println(t);
                }
            } else if ("FILTER".equals(command)) {
                String prefix = scanner.next();
                List<Tranzactie> filtered = deserializedTranzactii.stream()
                        .filter(t -> t.getData().startsWith(prefix))
                        .collect(Collectors.toList());
                if (filtered.isEmpty()) {
                    System.out.println("Niciun rezultat.");
                } else {
                    for (Tranzactie t : filtered) {
                        System.out.println(t);
                    }
                }
            } else if ("NOTE".equals(command)) {
                int id = scanner.nextInt();
                Tranzactie found = null;
                for (Tranzactie t : deserializedTranzactii) {
                    if (t.getId() == id) {
                        found = t;
                        break;
                    }
                }
                if (found != null) {
                    System.out.println("NOTE[" + id + "]: " + found.getNote());
                } else {
                    System.out.println("NOTE[" + id + "]: not found");
                }
            }
        }
    }
}
