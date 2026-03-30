package com.pao.laboratory06.exercise3;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // 1. Afisare constante financiare
        System.out.println("=== 1. Constante Financiare ===");
        System.out.println("Valoare TVA curent: " + ConstanteFinanciare.TVA.getValoare());
        System.out.println("Salariu minim: " + ConstanteFinanciare.SALARIU_MINIM.getValoare() + " lei");
        System.out.println("Cota impozit:"+ ConstanteFinanciare.COTA_IMPOZIT.getValoare()+"\n");

        // 2. Creare si sortare array de Ingineri
        System.out.println("=== 2. Sortare Ingineri ===");
        Inginer[] ingineri = {
                new Inginer("Zaharia", "Ion", "0722111222", 6000, 2000),
                new Inginer("Avram", "Vasile", "0722333444", 8000, 5000),
                new Inginer("Popescu", "Andrei", null, 5000, 1000)
        };

        Arrays.sort(ingineri);
        System.out.println("Sortare naturala (alfabetic dupa nume):");
        System.out.println(Arrays.toString(ingineri));

        Arrays.sort(ingineri, new ComparatorInginerSalariu());
        System.out.println("Sortare alternativa (descrescator dupa salariu):");
        System.out.println(Arrays.toString(ingineri) + "\n");

        // 3. Demonstrare acces prin referința PlataOnline (fara acces la metodele din Inginer)
        System.out.println("=== 3. Acces prin referinta de baza (PlataOnline) ===");
        PlataOnline contBaza = ingineri[0];
        contBaza.autentificare("zaharia_ion", "parola123");
        System.out.println("Sold initial: " + contBaza.consultareSold());
        contBaza.efectuarePlata(500);
        System.out.println("Sold dupa plata (500): " + contBaza.consultareSold() + "\n");

        // 4. Tratare Eroare: Apelare trimiteSMS pe entitate fara capabilitate
        System.out.println("=== 4. Tratare Eroare: Fara capabilitate SMS ===");
        try {
            // contBaza este instanta de Inginer, care NU suporta PlataOnlineSMS
            contBaza.trimiteSMS("Salut!");
        } catch (UnsupportedOperationException e) {
            System.out.println("Excepție prinsa corect: " + e.getMessage() + "\n");
        }

        // 5. Demonstrare capabilitati SMS pe PersoanaJuridica
        System.out.println("=== 5. Capabilitați SMS și Edge Cases ===");
        PersoanaJuridica pjTech = new PersoanaJuridica("TechCorp", "SRL", "0700999888", 25000);
        PersoanaJuridica pjFaraTel = new PersoanaJuridica("NoPhone", "SRL", null, 10000);

        // Acces prin referinta interfetei extinse
        PlataOnlineSMS contSms = pjTech;
        contSms.autentificare("admin", "adminPass");

        // Trimitere valida
        contSms.trimiteSMS("Plata de 1500 lei a fost procesata.");

        // Edge case: mesaj invalid
        contSms.trimiteSMS("");

        // Edge case: entitate fara telefon
        pjFaraTel.trimiteSMS("Notificare plata");

        // Verificare stocare mesaje
        System.out.println("Istoric mesaje pentru TechCorp: " + pjTech.getSmsTrimise() + "\n");

        // 6. Tratare Eroare: Autentificare cu date invalide
        System.out.println("=== 6. Tratare Eroare: Autentificare invalida ===");
        try {
            contSms.autentificare(null, "parola");
        } catch (IllegalArgumentException e) {
            System.out.println("Exceptie prinsa corect: " + e.getMessage());
        }
    }
}