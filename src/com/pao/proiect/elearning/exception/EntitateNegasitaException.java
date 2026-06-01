package com.pao.proiect.elearning.exception;

/**
 * Exceptie custom. Va fi aruncata cand cautam o entitate (curs, utilizator)
 * dupa ID si aceasta nu exista in sistem.
 */
public class EntitateNegasitaException extends Exception {
    public EntitateNegasitaException(String mesaj) {
        super(mesaj);
    }
}
