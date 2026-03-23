package com.pao.laboratory05.angajati;

import java.util.Scanner;
/**
 * Exercise 3 — Angajați
 *
 * Cerințele complete se află în:
 *   src/com/pao/laboratory05/Readme.md  →  secțiunea "Exercise 3 — Angajați"
 *
 * Creează fișierele de la zero în acest pachet, apoi rulează Main.java
 * pentru a verifica output-ul așteptat din Readme.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AngajatService service = AngajatService.getInstance();

        while (true) {
            System.out.println("\n===== Gestionare Angajați =====");
            System.out.println("1. Adaugă angajat");
            System.out.println("2. Listare după salariu");
            System.out.println("3. Caută după departament");
            System.out.println("0. Ieșire");
            System.out.print("Opțiune: ");

            String opt = scanner.nextLine();

            switch (opt) {
                case "1":
                    System.out.print("Nume: ");
                    String nume = scanner.nextLine();

                    System.out.print("Departament (nume): ");
                    String numeDept = scanner.nextLine();

                    System.out.print("Departament (locatie): ");
                    String locatieDept = scanner.nextLine();

                    System.out.print("Salariu: ");
                    double salariu;
                    try {
                        salariu = Double.parseDouble(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Valoare salariu invalidă!");
                        break;
                    }

                    Departament dept = new Departament(numeDept, locatieDept);
                    Angajat ang = new Angajat(nume, dept, salariu);
                    service.addAngajat(ang);

                    System.out.println("Angajat adăugat: " + ang.getNume());
                    break;

                case "2":
                    System.out.println("--- Angajați după salariu (descrescător) ---");
                    service.listBySalary(); // folosește metoda deja definită în serviciu
                    break;

                case "3":
                    System.out.print("Departament: ");
                    String cautaDept = scanner.nextLine();
                    service.findByDepartament(cautaDept); // folosește metoda deja definită
                    break;

                case "0":
                    System.out.println("La revedere!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Opțiune invalidă!");
            }
        }
    }
}