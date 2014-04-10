package com.catan.main.persistence.database;

import com.catan.main.datamodel.User;
import com.catan.main.persistence.DataAccess;
import com.catan.main.persistence.DataAccessException;
import com.catan.main.persistence.ObjectCreator;
import com.catan.main.persistence.UserAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDatabaseAccess extends UserAccess<ResultSet, PreparedStatement> {

    private DatabaseContext dataContext;
    private UserDatabaseCreator creator;

    public UserDatabaseAccess(DatabaseContext dataContext) {
        this.dataContext = dataContext;
        creator = new UserDatabaseCreator();
    }

    @Override
    public DatabaseContext getDataContext() {
        return dataContext;
    }

    @Override
    public ObjectCreator<User, ResultSet> getObjectCreator() {
        return creator;
    }

    /**
     * prepares the sql statement with the appropriate select parameters
     * @return PreparedStatement
     */
    @Override
    protected PreparedStatement getSelectStatement() {
        return null;
    }

    /**
     * prepares the sql statement with select parameters for a single get
     * @param id the id
     * @return PreparedStatement
     * @throws com.catan.main.persistence.DataAccessException
     */
    @Override
    protected PreparedStatement getSingleSelectStatement(int id)
            throws DataAccessException {
        return null;
    }

    /**
     * prepares the sql statement with the appropriate insert parameters
     * @param input the input object
     * @return PreparedStatement
     * @throws DataAccessException
     */
    @Override
    protected PreparedStatement getInsertStatement(User input)
            throws DataAccessException {
        return null;
    }

    /**
     * prepares the sql statement with the appropriate update parameters
     * @param input the input object
     * @return PreparedStatement
     * @throws DataAccessException
     */
    @Override
    protected PreparedStatement getUpdateStatement(User input)
            throws DataAccessException {
        return null;
    }

    /**
     * prepares the sql statement with the appropriate delete parameters
     * @param input the input object
     * @return PreparedStatement
     * @throws DataAccessException
     */
    @Override
    protected PreparedStatement getDeleteStatement(User input)
            throws DataAccessException {
        return null;
    }

    /**
     * checks all of the parameters of the object to verify their validity
     * @param input the input object
     * @return PreparedStatement
     */
    @Override
    protected boolean checkParameters(User input) {
        return false;
    }
}
