package com.pao.proiect.elearning.exception;

/**
 * Exceptie custom
 * Va fi aruncata cand cineva incearca sa faca o actiune pentru care nu are drepturi
 * (ex: un cursant vrea sa vada lista de cursanti a unui curs).
 */
public class AccesInterzisException extends RuntimeException {
    public AccesInterzisException(String mesaj) {
        super(mesaj);
    }
}
