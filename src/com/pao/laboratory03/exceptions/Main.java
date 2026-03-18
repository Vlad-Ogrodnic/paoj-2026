package com.pao.laboratory03.exceptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Exercițiul 3 — Excepții (checked, unchecked, custom)
 * <p>
 * Creează în acest pachet (lângă Main.java) două clase de excepții custom,
 * apoi demonstrează-le aici.
 * <p>
 * PASUL 1 — Creează InvalidAgeException.java (fișier separat):
 * - Extinde RuntimeException (unchecked)
 * - Constructor cu String message → apelează super(message)
 * <p>
 * PASUL 2 — Creează DuplicateEntryException.java (fișier separat):
 * - Extinde RuntimeException (unchecked)
 * - Constructor cu String message → apelează super(message)
 * <p>
 * PASUL 3 — În acest Main.java, implementează și demonstrează:
 * <p>
 * a) UNCHECKED EXCEPTIONS — NullPointerException, ArrayIndexOutOfBoundsException:
 * - Creează o metodă riskyMethod() care aruncă NullPointerException
 * - Prinde-o cu try-catch, afișează mesajul erorii
 * - Adaugă un bloc finally care se execută mereu
 * <p>
 * b) CUSTOM EXCEPTIONS — InvalidAgeException, DuplicateEntryException:
 * - Creează o metodă validateAge(int age) care aruncă InvalidAgeException
 * dacă age < 0 sau age > 150
 * - Creează o metodă addToList(List<String> list, String name) care aruncă
 * DuplicateEntryException dacă name există deja în listă
 * - Demonstrează ambele cu try-catch
 * <p>
 * c) MULTI-CATCH:
 * - Prinde InvalidAgeException | DuplicateEntryException într-un singur catch
 * <p>
 * d) CATCH ORDERING:
 * - Demonstrează că prinderea specifică (InvalidAgeException) trebuie
 * să fie ÎNAINTE de cea generală (RuntimeException)
 * <p>
 * e) THROW vs THROWS:
 * - Creează o metodă cu semnătura: void process(int age) throws InvalidAgeException
 * - Apeleaz-o din main cu try-catch
 * <p>
 * Output așteptat:
 * <p>
 * === a) Unchecked — NullPointerException ===
 * Prins: Cannot invoke "String.length()" because "s" is null
 * Finally se execută mereu!
 * <p>
 * === b) Custom exceptions ===
 * InvalidAgeException: Vârsta -5 nu este validă (0-150)
 * DuplicateEntryException: 'Ana' există deja în listă
 * <p>
 * === c) Multi-catch ===
 * Excepție prinsă: Vârsta 200 nu este validă (0-150)
 * <p>
 * === d) Catch ordering (specific → general) ===
 * InvalidAgeException prinsă specific: Vârsta -1 nu este validă (0-150)
 * <p>
 * === e) Throw vs throws ===
 * Metoda process() a aruncat: Vârsta 999 nu este validă (0-150)
 */
public class Main {
    public static void main(String[] args) {

        System.out.println("=== a) Unchecked — NullPointerException ===");
        try {
            riskyMethod();
        } catch (NullPointerException e) {
            System.out.println("Prins: " + e.getMessage());
        } finally {
            System.out.println("Finally se execută mereu!");
        }

        System.out.println("\n=== b) Custom exceptions ===");
        try {
            validateAge(-67);
        } catch (InvalidAgeException e) {
            System.out.println("InvalidAgeException: " + e.getMessage());
        }

        List<String> names = new ArrayList<>();
        names.add("Ana");
        names.add("Mihai");
        try {
            addToList(names, "Ana");
        } catch (DuplicateEntryException e) {
            System.out.println("DuplicateEntryException: " + e.getMessage());
        }

        System.out.println("\n=== c) Multi-catch ===");
        try {
            validateAge(200);
            addToList(names, "Mihai");
        } catch (InvalidAgeException | DuplicateEntryException e) {
            System.out.println("Excepție prinsă: " + e.getMessage());
        }

        System.out.println("\n=== d) Catch ordering (specific → general) ===");
        try {
            validateAge(-67);
        } catch (InvalidAgeException e) {
            System.out.println("InvalidAgeException prinsă specific: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Prinsă generic: " + e.getMessage());
        }

        System.out.println("\n=== e) Throw vs throws ===");
        try {
            process(999);
        } catch (InvalidAgeException e) {
            System.out.println("Metoda process() a aruncat: " + e.getMessage());
        }
    }

    static void riskyMethod() {
        String s = null;
        s.length(); // NullPointerException
    }

    static void validateAge(int age) {
        if (age < 0 || age > 150) {
            throw new InvalidAgeException("Vârsta " + age + " nu este validă (0-150)");
        }
    }

    static void addToList(List<String> list, String name) {
        if (list.contains(name)) {
            throw new DuplicateEntryException("'" + name + "' există deja în listă");
        }
        list.add(name);
    }

    static void process(int age) throws InvalidAgeException {
        if (age < 0 || age > 150) {
            throw new InvalidAgeException("Vârsta " + age + " nu este validă (0-150)");
        }
        System.out.println("Procesat: " + age);
    }
}

