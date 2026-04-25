package com.pao.laboratory08.exercise2;

import com.pao.laboratory08.exercise1.Student;
import com.pao.laboratory08.exercise1.Adresa;
import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        System.out.println();

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
        System.out.print("Introdu pragul minim de varsta: ");
        if (scanner.hasNextInt()) {
            int prag = scanner.nextInt();
            List<Student> filtrati = new ArrayList<>();
            for (Student s : studenti) {
                if (s.getVarsta() >= prag) {
                    filtrati.add(s);
                }
            }

            try (BufferedWriter fout = new BufferedWriter(new FileWriter("src/com/pao/laboratory08/exercise2/rezultate.txt"))) {
                for (Student s : filtrati) {
                    fout.write(s.toString());
                    fout.newLine();
                }
            }

            System.out.println("Filtru: varsta >= " + prag);
            System.out.println("Rezultate: " + filtrati.size() + " studenti");
            System.out.println();
            for (Student s : filtrati) {
                System.out.println(s);
            }
            System.out.println();
            System.out.println("Scris in: src/com/pao/laboratory08/exercise2/rezultate.txt");
        }
    }
}
