package com.catan.main.persistence.sqlite;

import com.catan.main.datamodel.PersistenceModel;
import com.catan.main.persistence.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class DatabaseContext<T extends PersistenceModel> extends DataContext<T, PreparedStatement> {

    //region Fields
    private final String tablesExist = "SELECT name FROM sqlite_master WHERE type='table' AND name != 'sqlite_sequence'";
    private String dbName = "data/sqlite/catan.sqlite";
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
            scanner = new Scanner(new File("createtables.txt"));
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
     * makes sure that the sqlite has tables
     * @return true if tables exist
     * @throws SQLException
     */
    public boolean dataStoreExist() throws SQLException {
        try (PreparedStatement stat = getTablesExistsStatement()) {
            ResultSet reader = getConnection().createStatement().executeQuery(tablesExist);
            return reader.next();
        }
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
    public List<T> get(PreparedStatement preparedStatement, int timeout, ObjectCreator creator) throws DataAccessException {
        ResultSet rs = null;
        try {
            preparedStatement.setQueryTimeout(timeout);
            return creator.initializeMany(preparedStatement.executeQuery());
        } catch (SQLException e) {
            DataUtils.crashOnException(e);
        } finally {
            try {
                preparedStatement.close();
            } catch (Exception ex) {
                DataUtils.crashOnException(ex);
            }
        }
        return null;
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
        } finally {
            try {
                preparedStatement.close();
            } catch (Exception ex) {
                DataUtils.crashOnException(ex);
            }
        }
        if (methodType == MethodType.INSERT) {
            return getId();
        } else {
            return rowsAffected;
        }
    }
    /**
     * Resets the current DataContext
     */
    @Override
    public void reset(){
        Statement stat = null;
        Scanner scanner = null;
        try {
            startTransaction();
            scanner = new Scanner(new File("droptables.txt"));
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
        initializeDataStore();
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

    private PreparedStatement getTablesExistsStatement() {
        PreparedStatement stat = null;
        try {
            stat = getConnection().prepareStatement(tablesExist);
        } catch (SQLException e) {
            DataUtils.crashOnException(e);
        }
        return stat;
    }
    //endregion
}
