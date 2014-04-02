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
    public abstract void initialize();

    public abstract void initializeDataStore();

    public abstract void startTransaction();

    public abstract void endTransaction(boolean commit);

    public ResultObject get(PreparedStatement statement) {
        return get(statement, 150);
    }

    public abstract ResultObject get(PreparedStatement statement, int timeout);

    public abstract int execute(PreparedStatement statement, MethodType methodType);

    //endregion

    enum MethodType { SELECT, INSERT, UPDATE, DELETE }
}
