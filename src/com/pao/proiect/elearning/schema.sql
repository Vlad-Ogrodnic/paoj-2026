DROP TABLE IF EXISTS scoruri;
DROP TABLE IF EXISTS materiale;
DROP TABLE IF EXISTS module;
DROP TABLE IF EXISTS inscrieri;
DROP TABLE IF EXISTS cursuri;
DROP TABLE IF EXISTS utilizatori;

CREATE TABLE utilizatori (
    id INTEGER PRIMARY KEY,
    nume TEXT NOT NULL,
    email TEXT NOT NULL,
    rol TEXT NOT NULL,
    departament TEXT,
    an_studiu INTEGER
);

CREATE TABLE cursuri (
    id INTEGER PRIMARY KEY,
    titlu TEXT NOT NULL,
    profesor_id INTEGER NOT NULL,
    FOREIGN KEY (profesor_id) REFERENCES utilizatori(id)
);

CREATE TABLE module (
    id INTEGER PRIMARY KEY,
    curs_id INTEGER NOT NULL,
    titlu TEXT NOT NULL,
    FOREIGN KEY (curs_id) REFERENCES cursuri(id)
);

CREATE TABLE materiale (
    id INTEGER PRIMARY KEY,
    modul_id INTEGER NOT NULL,
    titlu TEXT NOT NULL,
    tip TEXT NOT NULL,
    FOREIGN KEY (modul_id) REFERENCES module(id)
);

CREATE TABLE inscrieri (
    curs_id INTEGER NOT NULL,
    cursant_id INTEGER NOT NULL,
    data_inscrierii TEXT NOT NULL,
    PRIMARY KEY (curs_id, cursant_id),
    FOREIGN KEY (curs_id) REFERENCES cursuri(id),
    FOREIGN KEY (cursant_id) REFERENCES utilizatori(id)
);

CREATE TABLE scoruri (
    cursant_id INTEGER NOT NULL,
    material_id INTEGER NOT NULL,
    valoare REAL NOT NULL,
    PRIMARY KEY (cursant_id, material_id),
    FOREIGN KEY (cursant_id) REFERENCES utilizatori(id),
    FOREIGN KEY (material_id) REFERENCES materiale(id)
);
