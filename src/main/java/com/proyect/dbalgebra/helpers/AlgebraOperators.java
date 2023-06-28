package com.proyect.dbalgebra.helpers;

public enum AlgebraOperators {
    SELECTION('σ'),
    PROJECT('∏'),
    UNION('∪'),
    INTERSECT('∩'),
    DIFF('−'),
    CARTESIAN('X'),
    RENAME('ρ'),
    REUNION('∞');

    private final char symbol;
    
    private AlgebraOperators(char name) {
        this.symbol = name;
    }


    public char symbol() {
        return symbol;
    }

}
