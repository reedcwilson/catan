package com.catan.main.persistence.file;

import com.catan.main.datamodel.commands.Command;
import com.catan.main.persistence.*;

public class CommandFileAccess extends CommandAccess<FileOperation> {

    private FileContext dataContext;
    private CommandFileCreator creator;
    private String path;

    public CommandFileAccess(FileContext dataContext) {
        this.dataContext = dataContext;
        this.creator = new CommandFileCreator();
        path = "./data/file/command/";
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
    protected FileOperation getInsertStatement(Command input) throws DataAccessException {
        return new FileOperation(path + dataContext.getNextSequence("command"), input);
    }

    @Override
    protected FileOperation getUpdateStatement(Command input) throws DataAccessException {
        return new FileOperation(path + input.getId(), input);
    }

    @Override
    protected FileOperation getDeleteStatement(Command input) throws DataAccessException {
        return new FileOperation(path + input.getId());
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
