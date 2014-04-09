package com.catan.main.persistence.file;

import com.catan.main.persistence.*;

public class FileContext extends DataContext<FileResult, FileCommandStatement> {

    //region Public Interface

    @Override
    public CommandAccess getCommandAccess() {
        return null;
    }

    @Override
    public UserAccess getUserAccess() {
        return null;
    }

    @Override
    public GameAccess getGameAccess() {
        return null;
    }

    /**
     * performs any single use initialization
     */
    @Override
    public void initialize() {

    }

    /**
     * creates tables / initializes file structure
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
     * performs a get operation based on the given command and timeout
     * @param fileCommandStatement command to execute
     * @param timeout length of time before throwing an exception
     * @return ResultSet an object of the parameter type (T)
     */
    @Override
    public FileResult get(FileCommandStatement fileCommandStatement, int timeout) {
        return null;
    }

    /**
     * executes the given command based on the methodType
     * @param fileCommandStatement command to execute
     * @param methodType MethodType (update, insert, delete)
     * @return rows affected or id of inserted object
     */
    @Override
    public int execute(FileCommandStatement fileCommandStatement, MethodType methodType) {
        return 0;
    }
    //endregion
}
