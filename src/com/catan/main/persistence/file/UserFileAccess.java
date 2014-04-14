package com.catan.main.persistence.file;

import com.catan.main.datamodel.User;
import com.catan.main.persistence.*;

public class UserFileAccess extends UserAccess<FileOperation<User>> {

    private FileContext dataContext;
    private UserFileCreator creator;
    private String path;

    public UserFileAccess(FileContext dataContext) {
        this.dataContext = dataContext;
        this.creator = new UserFileCreator();
        this.path = "data/file/user/";
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
    protected FileOperation<User> getSelectStatement() {
        return new FileOperation<>(path);
    }

    @Override
    protected FileOperation<User> getSingleSelectStatement(int id) throws DataAccessException {
        return new FileOperation<>(path + id);
    }

    @Override
    protected FileOperation<User> getInsertStatement(User input) throws DataAccessException {
        return new FileOperation<>(path + dataContext.getNextSequence("user"), input);
    }

    @Override
    protected FileOperation<User> getUpdateStatement(User input) throws DataAccessException {
        return new FileOperation<>(path + input.getId(), input);
    }

    @Override
    protected FileOperation<User> getDeleteStatement(User input) throws DataAccessException {
        return new FileOperation<>(path + input.getId());
    }

    /**
     * checks all of the parameters of the object to verify their validity
     *
     * @param input the input object
     * @return PreparedStatement
     */
    @Override
    protected boolean checkParameters(User input) {
        return DataUtils.checkArgument(input);
    }
}
