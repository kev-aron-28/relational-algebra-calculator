package com.proyect.dbalgebra.entities;

import org.springframework.stereotype.Component;

@Component
public class AlgebraRequest {
    public String algebra;
    
    public AlgebraRequest() {
        algebra = "";
    }

    public AlgebraRequest(String algebra) {
        this.algebra = algebra;
    }

    public String getAlgebra() {
        return algebra;
    }

    public void setAlgebra(String algebra) {
        this.algebra = algebra;
    }
    
}
