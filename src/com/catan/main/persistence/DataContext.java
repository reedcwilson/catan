package com.catan.main.persistence;

import com.catan.main.datamodel.PersistenceModel;

import java.util.List;

public abstract class DataContext<T extends PersistenceModel, PreparedStatement> {

    //region Properties
    public abstract CommandAccess getCommandAccess();

    public abstract UserAccess getUserAccess();

    public abstract GameAccess getGameAccess();
    //endregion

    //region Public Interface

    /**
     * initializes the given DataContext such as connection
     */
    public abstract void initialize();

    /**
     * creates tables or does any other initialization that may need to be done multiple times
     */
    public abstract void initializeDataStore() throws DataAccessException;

    /**
     * starts a transaction that can be rolled back
     */
    public abstract void startTransaction();

    /**
     * ends the current transaction
     * @param commit boolean whether or not to commit the transaction
     */
    public abstract void endTransaction(boolean commit);

    /**
     * performs a get operation based on the given statement
     * @param statement prepared statement or command
     * @return an object of the parameter type (T)
     */
    public List<T> get(PreparedStatement statement, ObjectCreator creator) throws DataAccessException {
        return get(statement, 15, creator);
    }

    /**
     * performs a get operation based on the given statement and timeout
     * @param statement prepared statement or command
     * @param timeout length of time before throwing an exception
     * @return an object of the parameter type (T)
     */
    public abstract List<T> get(PreparedStatement statement, int timeout, ObjectCreator creator) throws DataAccessException;

    /**
     * executes the given command or statement based on the methodType
     * @param statement prepared statement or command
     * @param methodType MethodType (update, insert, delete)
     * @return rows affected or id of inserted object
     */
    public abstract int execute(PreparedStatement statement, MethodType methodType) throws DataAccessException;

    /**
     * Resets the current DataContext
     */
    public abstract void reset();

    //endregion

    public enum MethodType { SELECT, INSERT, UPDATE, DELETE }
}
