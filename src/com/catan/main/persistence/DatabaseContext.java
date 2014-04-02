package com.catan.main.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseContext extends DataContext<ResultSet, PreparedStatement> {
    @Override
    public void initialize() {

    }

    @Override
    public void initializeDataStore() {

    }

    @Override
    public void startTransaction() {

    }

    @Override
    public void endTransaction(boolean commit) {

    }

    @Override
    public ResultSet get(PreparedStatement preparedStatement, int timeout) {
        return null;
    }

    @Override
    public int execute(PreparedStatement preparedStatement, MethodType methodType) {
        return 0;
    }
}
