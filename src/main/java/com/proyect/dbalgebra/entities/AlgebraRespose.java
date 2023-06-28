package com.proyect.dbalgebra.entities;

public class AlgebraRespose {
    String sqlQuery;
    String errorMessage;
    boolean error;
    
    public AlgebraRespose(){

    }
    
    public AlgebraRespose(String sqlQuery, String errorMessage, boolean error) {
        this.sqlQuery = sqlQuery;
        this.errorMessage = errorMessage;
        this.error = error;
    }

    public String getSqlQuery() {
        return sqlQuery;
    }

    public void setSqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    
    
}
