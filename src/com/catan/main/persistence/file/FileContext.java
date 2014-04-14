package com.catan.main.persistence.file;

import com.catan.main.datamodel.PersistenceModel;
import com.catan.main.persistence.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileContext<T extends PersistenceModel> extends DataContext<T, FileOperation> {

    //region Fields
    CommandFileAccess commandAccess;
    UserFileAccess userAccess;
    GameFileAccess gameAccess;
    //endregion

    //region Properties
    //endregion

    //region Public Interface

    public FileContext() {
        commandAccess = new CommandFileAccess(this);
        userAccess = new UserFileAccess(this);
        gameAccess = new GameFileAccess(this);
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
        try {

            File users = new File("./data/file/user");
            if (!users.exists()) {
                users.mkdirs();
            }
            File games = new File("./data/file/game");
            if (!games.exists()) {
                games.mkdirs();
            }
            File commands = new File("./data/file/command");
            if (!commands.exists()) {
                commands.mkdirs();
            }

            File counters = new File("./data/file/counter");
            if (!counters.exists()) {
                counters.mkdirs();
            }
            File userCounters = new File("./data/file/counters/user");
            if (!userCounters.exists()) {
                userCounters.createNewFile();
            }
            File gameCounters = new File("./data/file/counters/game");
            if (!gameCounters.exists()) {
                gameCounters.createNewFile();
            }
            File commandCounters = new File("./data/file/counters/command");
            if (!commandCounters.exists()) {
                commandCounters.createNewFile();
            }
        } catch (IOException e) {
            DataUtils.crashOnException(e);
        }
    }

    /**
     * Resets the current DataContext
     */
    @Override
    public void reset() {
        DataUtils.delete(new File("./data/file"));
        initializeDataStore();
    }

    /**
     * starts a transaction that can be rolled back
     */
    @Override
    public void startTransaction() {
    }

    /**
     * ends the current transaction
     *
     * @param commit boolean whether or not to commit the transaction
     */
    @Override
    public void endTransaction(boolean commit) {
    }

    /**
     * performs a get operation based on the given command and timeout
     *
     * @param operation query to run
     * @param timeout   length of time before throwing an exception
     * @return ResultSet an object of the parameter type (T)
     */
    @Override
    public List<T> get(FileOperation operation, int timeout, ObjectCreator creator) throws DataAccessException {
        File file = new File(operation.getFileName());
        if (!file.exists()) {
            throw new DataAccessException("File does not exist");
        }
        if (file.isFile()) {
            return creator.initializeMany(new File[] { file });
        } else {
            return creator.initializeMany(file.listFiles());
        }
    }

    /**
     * executes the given command based on the methodType
     *
     * @param operation  object to execute method on
     * @param methodType MethodType (update, insert, delete)
     * @return rows affected or id of inserted object
     */
    @Override
    public int execute(FileOperation operation, MethodType methodType) throws DataAccessException {
        switch (methodType) {
            case INSERT:
                serializeFile(operation);
                return 1;
            case UPDATE:
                DataUtils.delete(new File(operation.getFileName()));
                serializeFile(operation);
                return 1;
            case DELETE:
                DataUtils.delete(new File(operation.getFileName()));
                return 1;
            case SELECT:
                throw new DataAccessException("cannot execute query from this method");
            default:
                throw new DataAccessException("Did not recognize the method type: " + methodType);
        }
    }

    public int getNextSequence(String collection) {
        // TODO: I will do this. We just need a file that has the current index
        return -1;
    }

    //endregion

    //region Helper Methods
    private void serializeFile(FileOperation operation) {
        // TODO: serialize file
    }
    //endregion
}
