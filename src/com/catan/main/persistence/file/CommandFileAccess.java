package com.catan.main.persistence.file;

import com.catan.main.datamodel.commands.Command;
import com.catan.main.datamodel.game.Game;
import com.catan.main.persistence.*;

import java.io.File;

public class CommandFileAccess extends CommandAccess<FileOperation<Command>> {

    private FileContext dataContext;
    private CommandFileCreator creator;
    private String path;

    public CommandFileAccess(FileContext dataContext) {
        this.dataContext = dataContext;
        this.creator = new CommandFileCreator();
        path = "data/file/command/";
    }

    @Override
    public DataContext getDataContext() {
        return dataContext;
    }

    @Override
    public ObjectCreator getObjectCreator() {
        return creator;
    }

    @Override
    protected FileOperation<Command> getSelectStatement() {
        return new FileOperation<>(path);
    }

    @Override
    protected FileOperation<Command> getSingleSelectStatement(int id) throws DataAccessException {
        return new FileOperation<>(path + id);
    }

    @Override
    protected FileOperation<Command> getInsertStatement(Command input) throws DataAccessException {
        return new FileOperation<>(path + input.getGameId() + "/" + dataContext.getNextSequence("command"), input);
    }

    @Override
    protected FileOperation<Command> getUpdateStatement(Command input) throws DataAccessException {
        return new FileOperation<>(path + input.getId(), input);
    }

    @Override
    protected FileOperation<Command> getDeleteStatement(Command input) throws DataAccessException {
        return new FileOperation<>(path + input.getId());
    }

    @Override
    protected FileOperation<Command> getCommandsForGameStatement(Long gameId) {
        return new FileOperation<>(path + gameId + "/");
    }

    /**
     * checks all of the parameters of the object to verify their validity
     *
     * @param input the input object
     * @return PreparedStatement
     */
    @Override
    protected boolean checkParameters(Command input) {
        return DataUtils.checkArgument(input);
    }
}
