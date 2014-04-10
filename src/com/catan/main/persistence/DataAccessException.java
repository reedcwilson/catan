package com.catan.main.persistence;

public class DataAccessException extends Exception {

    //region Constructors
    public DataAccessException() {
        return;
    }

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(Throwable throwable) {
        super(throwable);
    }

    public DataAccessException(String message, Throwable throwable) {
        super(message, throwable);
    }
    //endregion
}
