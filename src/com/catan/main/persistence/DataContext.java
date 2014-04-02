package com.catan.main.persistence;

public abstract class DataContext<ResultObject, PreparedStatement> {

    //region Fields
    private CommandAccess commandAccess;
    private UserAccess userAccess;
    private GameAccess gameAccess;
    //endregion

    //region Properties
    public CommandAccess getCommandAccess() {
        return commandAccess;
    }

    public void setCommandAccess(CommandAccess commandAccess) {
        this.commandAccess = commandAccess;
    }

    public UserAccess getUserAccess() {
        return userAccess;
    }

    public void setUserAccess(UserAccess userAccess) {
        this.userAccess = userAccess;
    }

    public GameAccess getGameAccess() {
        return gameAccess;
    }

    public void setGameAccess(GameAccess gameAccess) {
        this.gameAccess = gameAccess;
    }
    //endregion

    //region Public Interface

    /**
     * initializes the given DataContext such as connection
     */
    public abstract void initialize();

    /**
     * creates tables or does any other initialization that may need to be done multiple times
     */
    public abstract void initializeDataStore();

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
    public ResultObject get(PreparedStatement statement) {
        return get(statement, 150);
    }

    /**
     * performs a get operation based on the given statement and timeout
     * @param statement prepared statement or command
     * @param timeout length of time before throwing an exception
     * @return an object of the parameter type (T)
     */
    public abstract ResultObject get(PreparedStatement statement, int timeout);

    /**
     * executes the given command or statement based on the methodType
     * @param statement prepared statement or command
     * @param methodType MethodType (update, insert, delete)
     * @return rows affected or id of inserted object
     */
    public abstract int execute(PreparedStatement statement, MethodType methodType);

    //endregion

    enum MethodType { SELECT, INSERT, UPDATE, DELETE }
}
