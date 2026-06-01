package com.pao.proiect.elearning;

import com.pao.proiect.elearning.exception.AccesInterzisException;
import com.pao.proiect.elearning.exception.EntitateNegasitaException;
import com.pao.proiect.elearning.model.*;
import com.pao.proiect.elearning.service.AuditService;
import com.pao.proiect.elearning.service.CursService;
import com.pao.proiect.elearning.service.UtilizatorService;
import com.pao.proiect.elearning.util.DatabaseConnection;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Locale;

public class Main {
    private static final Scanner scanner = new Scanner(System.in).useLocale(Locale.US);
    private static final UtilizatorService userService = UtilizatorService.getInstance();
    private static final CursService cursService = CursService.getInstance();
    private static final AuditService auditService = AuditService.getInstance();

    public static void main(String[] args) {
        DatabaseConnection.getInstance().initializeSchema();
        incarcaDateInitiale();

        boolean ruleaza = true;
        while (ruleaza) {
            afiseazaMeniu();
            int optiune = citesteIntreg("Alege o optiune: ");

            try {
                switch (optiune) {
                    case 1:
                        adaugaUtilizatorNou();
                        break;
                    case 2:
                        creeazaCursNou();
                        break;
                    case 3:
                        inscrieCursant();
                        break;
                    case 4:
                        adaugaModul();
                        break;
                    case 5:
                        adaugaMaterial();
                        break;
                    case 6:
                        inregistreazaNota();
                        break;
                    case 7:
                        afiseazaCursurileProfesorului();
                        break;
                    case 8:
                        afiseazaCursantiiInscrisi();
                        break;
                    case 9:
                        calculeazaMedia();
                        break;
                    case 10:
                        stergeCurs();
                        break;
                    case 0:
                        ruleaza = false;
                        System.out.println("La revedere!");
                        break;
                    default:
                        System.out.println("Optiune invalida. Incercati din nou.");
                }
            } catch (EntitateNegasitaException | AccesInterzisException | IllegalArgumentException e) {
                System.out.println("--- " + e.getMessage() + " ---");
            } catch (Exception e) {
                System.out.println("A aparut o eroare neasteptata: " + e.getMessage());
                scanner.nextLine();
            }
        }
    }

    private static void afiseazaMeniu() {
        System.out.println("\n=== PLATFORMA E-LEARNING ===");
        System.out.println("1. Adauga un utilizator nou (Profesor/Cursant)");
        System.out.println("2. Creeaza un curs nou");
        System.out.println("3. Inscrie un cursant la un curs");
        System.out.println("4. Adauga un modul intr-un curs");
        System.out.println("5. Adauga un material la un modul");
        System.out.println("6. Inregistreaza nota la un quiz");
        System.out.println("7. Afiseaza cursurile unui profesor");
        System.out.println("8. Afiseaza cursantii inscrisi la un curs (sortati alfabetic)");
        System.out.println("9. Calculeaza media unui cursant la un curs");
        System.out.println("10. Sterge un curs");
        System.out.println("0. Iesire");
        System.out.println("============================");
    }

    private static void adaugaUtilizatorNou() {
        auditService.logActiune("adauga_utilizator");
        System.out.println("Tip utilizator (1-Profesor, 2-Cursant): ");
        int tip = citesteIntreg("");
        int id = citesteIntreg("ID: ");
        System.out.print("Nume: ");
        String nume = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();

        if (tip == 1) {
            System.out.print("Departament: ");
            String dep = scanner.nextLine();
            userService.adaugaUtilizator(new Profesor(id, nume, email, dep));
        } else {
            int an = citesteIntreg("An studiu: ");
            userService.adaugaUtilizator(new Cursant(id, nume, email, an));
        }
        System.out.println("Utilizator adaugat!");
    }

    private static void creeazaCursNou() {
        auditService.logActiune("creeaza_curs");
        int id = citesteIntreg("ID Curs: ");
        System.out.print("Titlu Curs: ");
        String titlu = scanner.nextLine();
        afiseazaProfesoriDisponibili();
        int idProf = citesteIdProfesorExistent("ID Profesor titular: ");
        cursService.adaugaCurs(new Curs(id, titlu, idProf));
        System.out.println("Curs creat!");
    }

    private static void inscrieCursant() throws EntitateNegasitaException {
        auditService.logActiune("inscrie_cursant");
        afiseazaCursuriDisponibile();
        int idCurs = citesteIdCursExistent("ID Curs: ");
        afiseazaUtilizatoriDisponibili();
        int idCursant = citesteIdUtilizatorExistent("ID Cursant: ");
        cursService.inscrieCursant(idCurs, idCursant);
        System.out.println("Inscriere realizata!");
    }

    private static void adaugaModul() throws EntitateNegasitaException {
        auditService.logActiune("adauga_modul");
        afiseazaCursuriDisponibile();
        int idCurs = citesteIdCursExistent("ID Curs: ");
        int idModul = citesteIntreg("ID Noul Modul: ");
        System.out.print("Titlu Modul: ");
        String titlu = scanner.nextLine();
        cursService.adaugaModulLaCurs(idCurs, new Modul(idModul, titlu));
        System.out.println("Modul adaugat!");
    }

    private static void adaugaMaterial() throws EntitateNegasitaException {
        auditService.logActiune("adauga_material");
        afiseazaCursuriDisponibile();
        int idCurs = citesteIdCursExistent("ID Curs: ");
        afiseazaModuleDisponibile(idCurs);
        int idModul = citesteIdModulExistent(idCurs);
        int idMat = citesteIntreg("ID Noul Material: ");
        System.out.print("Titlu Material: ");
        String titlu = scanner.nextLine();

        String tip = "";
        while (true) {
            System.out.print("Tip (VIDEO, PDF, QUIZ): ");
            tip = scanner.nextLine().toUpperCase();
            if (tip.equals("VIDEO") || tip.equals("PDF") || tip.equals("QUIZ")) {
                break;
            }
            System.out.println("   -> EROARE: Tip invalid! Alegeti doar dintre VIDEO, PDF sau QUIZ.");
        }

        cursService.adaugaMaterialLaModul(idCurs, idModul, new Material(idMat, titlu, tip));
        System.out.println("Material adaugat!");
    }

    private static void inregistreazaNota() {
        auditService.logActiune("inregistreaza_nota");
        afiseazaCursantiDisponibili();
        int idCursant = citesteIdCursantExistent("ID Cursant: ");
        afiseazaToateQuizurile();
        int idQuiz = citesteIdQuizExistent("ID Material (Quiz): ");
        System.out.print("Nota: ");
        double nota = scanner.nextDouble();
        scanner.nextLine();
        cursService.adaugaScor(idCursant, idQuiz, nota);
        System.out.println("Scor adaugat!");
    }

    private static void afiseazaCursurileProfesorului() {
        auditService.logActiune("afiseaza_cursuri_profesor");
        afiseazaProfesoriDisponibili();
        int idProf = citesteIdProfesorExistent("ID Profesor: ");
        List<CursCuProfesor> cursuri = cursService.getCursuriProfesorCuDetalii(idProf);
        if (cursuri.isEmpty()) {
            System.out.println("Acest profesor nu are cursuri.");
        } else {
            System.out.println("Cursurile profesorului:");
            for (CursCuProfesor c : cursuri) System.out.println(" - " + c);
        }
    }

    private static void afiseazaCursantiiInscrisi() throws EntitateNegasitaException {
        auditService.logActiune("afiseaza_cursanti_inscrisi");
        afiseazaCursuriDisponibile();
        int idCurs = citesteIdCursExistent("ID Curs: ");
        afiseazaProfesoriDisponibili();
        int idProf = citesteIdProfesorExistent("ID-ul tau (trebuie sa fii profesorul cursului): ");
        
        List<Utilizator> cursanti = cursService.getCursantiInscrisi(idCurs, idProf);
        
        Collections.sort(cursanti, new Comparator<Utilizator>() {
            @Override
            public int compare(Utilizator u1, Utilizator u2) {
                return u1.getNume().compareToIgnoreCase(u2.getNume());
            }
        });

        if (cursanti.isEmpty()) {
            System.out.println("Niciun cursant inscris.");
        } else {
            System.out.println("Cursanti inscrisi (ordonati alfabetic):");
            for (Utilizator u : cursanti) System.out.println(" - " + u.getNume());
        }
    }

    private static void calculeazaMedia() throws EntitateNegasitaException {
        auditService.logActiune("calculeaza_media");
        afiseazaCursuriDisponibile();
        int idCurs = citesteIdCursExistent("ID Curs: ");
        afiseazaUtilizatoriDisponibili();
        int idCursant = citesteIdUtilizatorExistent("ID Cursant: ");
        double media = cursService.calculeazaMediaCursant(idCurs, idCursant);
        System.out.println("Media este: " + media);
    }

    private static void stergeCurs() {
        auditService.logActiune("sterge_curs");
        afiseazaCursuriDisponibile();
        int idCurs = citesteIdCursExistent("ID Curs de sters: ");
        cursService.stergeCurs(idCurs);
        System.out.println("Curs sters!");
    }

    private static void afiseazaUtilizatoriDisponibili() {
        System.out.println("   [i] Utilizatori in sistem:");
        boolean gasit = false;
        for (Utilizator u : userService.getTotiUtilizatorii()) {
            System.out.println("       ID: " + u.getId() + " | Nume: " + u.getNume() + " (" + u.getRol() + ")");
            gasit = true;
        }
        if (!gasit) System.out.println("       Niciun utilizator.");
    }

    private static void afiseazaProfesoriDisponibili() {
        System.out.println("   [i] Profesori in sistem:");
        boolean gasit = false;
        for (Utilizator u : userService.getTotiUtilizatorii()) {
            if ("PROFESOR".equals(u.getRol())) {
                Profesor p = (Profesor) u;
                System.out.println("       ID: " + p.getId() + " | Nume: " + p.getNume() + " | Departament: " + p.getDepartament());
                gasit = true;
            }
        }
        if (!gasit) System.out.println("       Niciun profesor inregistrat.");
    }

    private static void afiseazaCursantiDisponibili() {
        System.out.println("   [i] Cursanti in sistem:");
        boolean gasit = false;
        for (Utilizator u : userService.getTotiUtilizatorii()) {
            if ("CURSANT".equals(u.getRol())) {
                System.out.println("       ID: " + u.getId() + " | Nume: " + u.getNume());
                gasit = true;
            }
        }
        if (!gasit) System.out.println("       Niciun cursant inregistrat.");
    }

    private static void afiseazaCursuriDisponibile() {
        System.out.println("   [i] Cursuri in sistem:");
        boolean gasit = false;
        for (Curs c : cursService.getToateCursurile()) {
            System.out.println("       ID: " + c.getId() + " | Titlu: " + c.getTitlu());
            gasit = true;
        }
        if (!gasit) System.out.println("       Niciun curs.");
    }

    private static void afiseazaModuleDisponibile(int idCurs) {
        try {
            Curs curs = cursService.cautaCurs(idCurs);
            List<Modul> module = curs.getModule();
            if (module.isEmpty()) {
                System.out.println("   [i] Acest curs nu are niciun modul inca.");
            } else {
                System.out.println("   [i] Module in cursul '" + curs.getTitlu() + "':");
                for (Modul m : module) {
                    System.out.println("       ID: " + m.getId() + " | Titlu: " + m.getTitlu());
                }
            }
        } catch (EntitateNegasitaException e) {
        }
    }

    private static void afiseazaToateQuizurile() {
        System.out.println("   [i] Quiz-uri disponibile in sistem:");
        boolean gasit = false;
        for (Curs c : cursService.getToateCursurile()) {
            for (Modul m : c.getModule()) {
                for (Material mat : m.getMateriale()) {
                    if ("QUIZ".equalsIgnoreCase(mat.getTip())) {
                        System.out.println("       ID Quiz: " + mat.getId() + " | Titlu: " + mat.getTitlu() + " (din cursul: " + c.getTitlu() + ")");
                        gasit = true;
                    }
                }
            }
        }
        if (!gasit) System.out.println("       Nu exista niciun quiz inregistrat in sistem.");
    }

    private static int citesteIntreg(String mesaj) {
        int numar = -1;
        boolean valid = false;
        while (!valid) {
            System.out.print(mesaj);
            try {
                numar = scanner.nextInt();
                valid = true;
            } catch (Exception e) {
                System.out.println("Te rog introdu un numar valid!");
            } finally {
                scanner.nextLine();
            }
        }
        return numar;
    }

    private static int citesteIdCursExistent(String mesaj) {
        while (true) {
            int id = citesteIntreg(mesaj);
            if (id == 0) throw new IllegalArgumentException("Operatiune anulata de utilizator.");
            try {
                cursService.cautaCurs(id);
                return id;
            } catch (EntitateNegasitaException e) {
                System.out.println("   -> EROARE: " + e.getMessage() + " (sau introduceti 0 pentru anulare). Reincercati.");
            }
        }
    }

    private static int citesteIdUtilizatorExistent(String mesaj) {
        while (true) {
            int id = citesteIntreg(mesaj);
            if (id == 0) throw new IllegalArgumentException("Operatiune anulata de utilizator.");
            try {
                userService.cautaUtilizator(id);
                return id;
            } catch (EntitateNegasitaException e) {
                System.out.println("   -> EROARE: " + e.getMessage() + " (sau introduceti 0 pentru anulare). Reincercati.");
            }
        }
    }

    private static int citesteIdProfesorExistent(String mesaj) {
        while (true) {
            int id = citesteIntreg(mesaj);
            if (id == 0) throw new IllegalArgumentException("Operatiune anulata de utilizator.");
            try {
                Utilizator u = userService.cautaUtilizator(id);
                if ("PROFESOR".equals(u.getRol())) {
                    return id;
                } else {
                    System.out.println("   -> EROARE: Utilizatorul cu ID " + id + " nu este profesor. (sau introduceti 0 pentru anulare). Reincercati.");
                }
            } catch (EntitateNegasitaException e) {
                System.out.println("   -> EROARE: " + e.getMessage() + " (sau introduceti 0 pentru anulare). Reincercati.");
            }
        }
    }

    private static int citesteIdCursantExistent(String mesaj) {
        while (true) {
            int id = citesteIntreg(mesaj);
            if (id == 0) throw new IllegalArgumentException("Operatiune anulata de utilizator.");
            try {
                Utilizator u = userService.cautaUtilizator(id);
                if ("CURSANT".equals(u.getRol())) {
                    return id;
                } else {
                    System.out.println("   -> EROARE: Utilizatorul cu ID " + id + " nu este cursant. (sau introduceti 0 pentru anulare). Reincercati.");
                }
            } catch (EntitateNegasitaException e) {
                System.out.println("   -> EROARE: " + e.getMessage() + " (sau introduceti 0 pentru anulare). Reincercati.");
            }
        }
    }

    private static int citesteIdQuizExistent(String mesaj) {
        while (true) {
            int idQuiz = citesteIntreg(mesaj);
            if (idQuiz == 0) throw new IllegalArgumentException("Operatiune anulata de utilizator.");
            boolean gasit = false;
            for (Curs c : cursService.getToateCursurile()) {
                for (Modul m : c.getModule()) {
                    for (Material mat : m.getMateriale()) {
                        if (mat.getId() == idQuiz && "QUIZ".equalsIgnoreCase(mat.getTip())) {
                            gasit = true;
                            break;
                        }
                    }
                }
            }
            if (gasit) return idQuiz;
            System.out.println("   -> EROARE: Materialul cu ID " + idQuiz + " nu exista sau nu este un QUIZ. (sau introduceti 0 pentru anulare). Reincercati.");
        }
    }

    private static int citesteIdModulExistent(int idCurs) {
        while (true) {
            int idModul = citesteIntreg("ID Modul existent: ");
            if (idModul == 0) throw new IllegalArgumentException("Operatiune anulata de utilizator.");
            try {
                Curs curs = cursService.cautaCurs(idCurs);
                boolean gasit = false;
                for (Modul m : curs.getModule()) {
                    if (m.getId() == idModul) {
                        gasit = true;
                        break;
                    }
                }
                if (gasit) return idModul;
                System.out.println("   -> EROARE: Modulul nu exista in acest curs. (sau introduceti 0 pentru anulare). Reincercati.");
            } catch (EntitateNegasitaException e) {
            }
        }
    }

    private static void incarcaDateInitiale() {
        userService.adaugaUtilizator(new Profesor(1, "Ion Popescu", "ion@univ.ro", "Informatica"));
        userService.adaugaUtilizator(new Cursant(100, "Maria", "maria@stud.ro", 2));
        cursService.adaugaCurs(new Curs(10, "Java OOP", 1));
        System.out.println("(*) S-au incarcat date de test: Prof ID=1, Cursant ID=100, Curs ID=10.");
    }
}