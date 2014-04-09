package com.catan.main.persistence;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseContext extends DataContext<ResultSet, PreparedStatement> {

    //region Fields
    private String dbName = "data" + File.separator + "catan.sqlite";
    private String connectionUrl = "jdbc:sqlite:" + dbName;
    private Connection connection;
    //endregion

    //region Properties
    public Connection getConnection() {
        return connection;
    }
    private void setConnection(Connection connection) {
        this.connection = connection;
    }
    //endregion

    //region Public Interface

    /**
     * initializes the connection
     */
    @Override
    public void initialize() {
        try {
            // register the driver
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * creates tables
     */
    @Override
    public void initializeDataStore() {

    }

    /**
     * starts a transaction that can be rolled back
     */
    @Override
    public void startTransaction() {

    }

    /**
     * ends the current transaction
     * @param commit boolean whether or not to commit the transaction
     */
    @Override
    public void endTransaction(boolean commit) {

    }

    /**
     * performs a get operation based on the given statement and timeout
     * @param preparedStatement prepared statement
     * @param timeout length of time before throwing an exception
     * @return ResultSet an object of the parameter type (T)
     */
    @Override
    public ResultSet get(PreparedStatement preparedStatement, int timeout) {
        return null;
    }

    /**
     * executes the given statement based on the methodType
     * @param preparedStatement prepared statement or command
     * @param methodType MethodType (update, insert, delete)
     * @return rows affected or id of inserted object
     */
    @Override
    public int execute(PreparedStatement preparedStatement, MethodType methodType) {
        return 0;
    }
    //endregion
}
