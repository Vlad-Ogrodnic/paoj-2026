package com.pao.laboratory08.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        List<Student> studenti = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String nume = parts[0].trim();
                    int varsta = Integer.parseInt(parts[1].trim());
                    String oras = parts[2].trim();
                    String strada = parts[3].trim();
                    studenti.add(new Student(nume, varsta, new Adresa(oras, strada)));
                }
            }
        }

        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextLine()) {
            String fullCommand = scanner.nextLine();
            String[] cmdParts = fullCommand.split(" ", 2);
            String command = cmdParts[0];

            if (command.equals("PRINT")) {
                for (Student s : studenti) {
                    System.out.println(s);
                }
            } else if (command.equals("SHALLOW")) {
                String nume = cmdParts[1];
                Student original = null;
                for (Student s : studenti) {
                    if (s.getNume().equals(nume)) {
                        original = s;
                        break;
                    }
                }
                if (original != null) {
                    Student clona = original.shallowClone();
                    clona.getAdresa().setOras("MODIFICAT");
                    System.out.println("Original: " + original);
                    System.out.println("Clona: " + clona);
                }
            } else if (command.equals("DEEP")) {
                String nume = cmdParts[1];
                Student original = null;
                for (Student s : studenti) {
                    if (s.getNume().equals(nume)) {
                        original = s;
                        break;
                    }
                }
                if (original != null) {
                    Student clona = original.deepClone();
                    clona.getAdresa().setOras("MODIFICAT");
                    System.out.println("Original: " + original);
                    System.out.println("Clona: " + clona);
                }
            }
        }
    }
}
