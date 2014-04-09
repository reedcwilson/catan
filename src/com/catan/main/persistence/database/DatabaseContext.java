package com.catan.main.persistence.database;

import com.catan.main.persistence.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

public class DatabaseContext extends DataContext<ResultSet, PreparedStatement> {

    //region Fields
    private String dbName = "catan.sqlite";
    private String connectionUrl = "jdbc:sqlite:" + dbName;
    private Connection connection;
    private CommandDatabaseAccess commandAccess;
    private UserDatabaseAccess userAccess;
    private GameDatabaseAccess gameAccess;
    //endregion

    //region Properties
    public Connection getConnection() {
        return connection;
    }
    private void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public CommandAccess getCommandAccess() {
        return commandAccess;
    }

    @Override
    public UserAccess getUserAccess() {
        return userAccess;
    }

    @Override
    public GameAccess getGameAccess() {
        return gameAccess;
    }
    //endregion

    //region Public Interface

    public DatabaseContext() {
        commandAccess = new CommandDatabaseAccess(this);
        userAccess = new UserDatabaseAccess(this);
        gameAccess = new GameDatabaseAccess(this);
    }

    /**
     * initializes the connection
     */
    @Override
    public void initialize() {
        try {
            // register the driver
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            DataUtils.crashOnException(e);
        }
    }

    /**
     * creates tables
     */
    @Override
    public void initializeDataStore() {
        Statement stat = null;
        Scanner scanner = null;
        try {
            startTransaction();
            scanner = new Scanner(new File("database.txt"));
            while (scanner.hasNextLine()) {
                stat = connection.createStatement();
                String sql = scanner.nextLine();
                if (!sql.trim().isEmpty())
                    stat.executeUpdate(sql);
            }
        } catch (SQLException | FileNotFoundException e) {
            endTransaction(false);
            DataUtils.crashOnException(e);
        } finally {
            try {
                if (scanner != null) {
                    scanner.close();
                }
                if (stat != null) {
                    stat.close();
                }
            } catch (Exception e) {
                DataUtils.crashOnException(e);
            }
        }
        endTransaction(true);
    }

    /**
     * starts a transaction that can be rolled back
     */
    @Override
    public void startTransaction() {
        try {
            setConnection(DriverManager.getConnection(connectionUrl));
            this.connection.setAutoCommit(false);
        } catch (SQLException ex) {
            DataUtils.crashOnException(ex);
        }
    }

    /**
     * ends the current transaction
     *
     * @param commit boolean whether or not to commit the transaction
     */
    @Override
    public void endTransaction(boolean commit) {
        try {
            if (commit) {
                connection.commit();
            } else {
                connection.rollback();
            }
        } catch (SQLException e) {
            DataUtils.crashOnException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                DataUtils.crashOnException(e);
            }
        }
        connection = null;
    }

    /**
     * performs a get operation based on the given statement and timeout
     *
     * @param preparedStatement prepared statement
     * @param timeout           length of time before throwing an exception
     * @return ResultSet an object of the parameter type (T)
     */
    @Override
    public ResultSet get(PreparedStatement preparedStatement, int timeout) {
        ResultSet rs = null;
        try {
            preparedStatement.setQueryTimeout(timeout);
            rs = preparedStatement.executeQuery();
        } catch (SQLException e) {
            DataUtils.crashOnException(e);
        }
        return rs;
    }

    /**
     * executes the given statement based on the methodType
     *
     * @param preparedStatement prepared statement or command
     * @param methodType        MethodType (update, insert, delete)
     * @return rows affected or id of inserted object
     */
    @Override
    public int execute(PreparedStatement preparedStatement, MethodType methodType) {
        int rowsAffected = 0;
        try {
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            DataUtils.crashOnException(e);
        }
        if (methodType == MethodType.INSERT) {
            return getId();
        } else {
            return rowsAffected;
        }
    }
    //endregion

    //region Helper Methods
    private int getId() {
        int id = -1;
        ResultSet rowId = null;
        try {
            Statement stat = this.connection.createStatement();
            rowId = stat.executeQuery("select last_insert_rowid()");
            rowId.next();
            id = rowId.getInt(1);
        } catch (SQLException e) {
            DataUtils.crashOnException(e);
        } finally {
            try {
                if (rowId != null) {
                    rowId.close();
                }
            } catch (SQLException e) {
                DataUtils.crashOnException(e);
            }
        }
        return id;
    }
    //endregion
}
