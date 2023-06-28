package com.proyect.dbalgebra.helpers;

public enum SQLoperators {
    SELECT("SELECT"),
    WHERE("WHERE"),
    FROM("FROM"),
    INNERJOIN("INNER JOIN"),
    ON("ON");

    private final String symbol;
    
    private SQLoperators(String name) {
        this.symbol = name;
    }

    public String symbol() {
        return symbol;
    }

}
