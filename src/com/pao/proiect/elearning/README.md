# Platforma E-Learning

Am ales ca tema pentru proiect platforma e-learning

## 1.1 Actiuni posibile in sistem 
1. Adauga un utilizator nou (Profesor sau Cursant) in sistem.
2. Creeaza un curs nou (asociat unui profesor).
3. Inscrie un cursant la un curs.
4. Adauga un modul intr-un curs.
5. Adauga un material de studiu (sau quiz) la un modul.
6. Inregistreaza nota unui cursant la un quiz.
7. Afiseaza toate cursurile predate de un profesor.
8. Afiseaza toti cursantii inscrisi la un curs.
9. Calculeaza media unui cursant la un anumit curs.
10. Sterge un curs din sistem.

## 1.2 Tipuri de obiecte 
1. `Utilizator` (clasa abstracta)
2. `Profesor` (mosteneste Utilizator)
3. `Cursant` (mosteneste Utilizator)
4. `Curs` 
5. `Modul` (o sectiune dintr-un curs)
6. `Material` (poate fi video, document, etc.)
7. `Inscriere` (clasa imutabila care asociaza un cursant cu un curs)
8. `Scor` (reprezinta nota obtinuta de un cursant la o evaluare)
