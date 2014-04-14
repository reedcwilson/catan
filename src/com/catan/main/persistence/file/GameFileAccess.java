package com.catan.main.persistence.file;

import com.catan.main.datamodel.commands.Command;
import com.catan.main.datamodel.game.Game;
import com.catan.main.persistence.*;

public class GameFileAccess extends GameAccess<FileOperation> {

    private FileContext dataContext;
    private GameFileCreator creator;
    private String path;

    public GameFileAccess(FileContext dataContext) {
        this.dataContext = dataContext;
        this.creator = new GameFileCreator();
        this.path = "./data/file/game/";
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
    protected FileOperation getSelectStatement() {
        return new FileOperation(path);
    }

    @Override
    protected FileOperation getSingleSelectStatement(int id) throws DataAccessException {
        return new FileOperation(path + id);
    }

    @Override
    protected FileOperation getInsertStatement(Game input) throws DataAccessException {
        return new FileOperation(path + dataContext.getNextSequence("game"), input);
    }

    @Override
    protected FileOperation getUpdateStatement(Game input) throws DataAccessException {
        return new FileOperation(path + input.getId(), input);
    }

    @Override
    protected FileOperation getDeleteStatement(Game input) throws DataAccessException {
        return new FileOperation(path + input.getId());
    }

    /**
     * checks all of the parameters of the object to verify their validity
     *
     * @param input the input object
     * @return PreparedStatement
     */
    @Override
    protected boolean checkParameters(Game input) {
        return DataUtils.checkArgument(input);
    }

    private Command getLatestCommand() throws DataAccessException {
        return null;
    }
}
