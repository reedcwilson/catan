package com.catan.main.persistence.sqlite;

import com.catan.main.datamodel.User;
import com.catan.main.persistence.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDatabaseAccess extends UserAccess<PreparedStatement> {

    private static final String singleSelectSql = "SELECT * FROM User where id=?";
    private static final String selectSql = "SELECT * FROM User";
    private static final String updateSql = "UPDATE User SET name=?, password=? where id=?";
    private static final String insertSql = "INSERT INTO User ('name', 'password') VALUES (?, ?)";
    private static final String deleteSql = "DELETE FROM User WHERE id=?";

    private DatabaseContext dataContext;
    private UserDatabaseCreator creator;

    public UserDatabaseAccess(DatabaseContext dataContext) {
        this.dataContext = dataContext;
        creator = new UserDatabaseCreator();
    }

    @Override
    public DataContext getDataContext() {
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
        PreparedStatement stat = null;
        try {
            stat = dataContext.getConnection().prepareStatement(selectSql);
        } catch (SQLException e) {
            DataUtils.crashOnException(e);
        }
        return stat;
    }

    /**
     * prepares the sql statement with select parameters for a single get
     * @param id the id
     * @return PreparedStatement
     * @throws com.catan.main.persistence.DataAccessException
     */
    @Override
    protected PreparedStatement getSingleSelectStatement(int id) throws DataAccessException {
        if (DataUtils.checkArgument(id)) {
            PreparedStatement stat = null;
            try {
                stat = dataContext.getConnection().prepareStatement(singleSelectSql);
                stat.setInt(1, id);
            } catch (SQLException e) {
                DataUtils.crashOnException(e);
            }
            return stat;
        } else {
            throw new DataAccessException("Invalid id given.");
        }
    }

    /**
     * prepares the sql statement with the appropriate insert parameters
     * @param input the input object
     * @return PreparedStatement
     * @throws DataAccessException
     */
    @Override
    protected PreparedStatement getInsertStatement(User input) throws DataAccessException {
        if (checkParameters(input)) {
            PreparedStatement stat = null;
            try {
                stat = dataContext.getConnection().prepareStatement(insertSql);
                stat.setString(1, input.getName());
                stat.setString(2, input.getPassword());
            } catch (SQLException e) {
                DataUtils.crashOnException(e);
            }
            return stat;
        } else {
            throw new DataAccessException("Invalid command parameters. Could not save to sqlite");
        }
    }

    /**
     * prepares the sql statement with the appropriate update parameters
     * @param input the input object
     * @return PreparedStatement
     * @throws DataAccessException
     */
    @Override
    protected PreparedStatement getUpdateStatement(User input) throws DataAccessException {
        if (checkParameters(input)) {
            PreparedStatement stat = null;
            try {
                stat = dataContext.getConnection().prepareStatement(updateSql);
                stat.setString(1, input.getName());
                stat.setString(2, input.getPassword());
                stat.setInt(3, input.getId().intValue());
            } catch (SQLException e) {
                DataUtils.crashOnException(e);
            }
            return stat;
        } else {
            throw new DataAccessException("Invalid command parameters. Could not save to sqlite");
        }
    }

    /**
     * prepares the sql statement with the appropriate delete parameters
     * @param input the input object
     * @return PreparedStatement
     * @throws DataAccessException
     */
    @Override
    protected PreparedStatement getDeleteStatement(User input) throws DataAccessException {
        PreparedStatement stat = null;
        if (checkParameters(input)) {
            try {
                stat = dataContext.getConnection().prepareStatement(deleteSql);
                stat.setInt(1, input.getId().intValue());
            } catch (SQLException e) {
                DataUtils.crashOnException(e);
            }
            return stat;
        } else {
            throw new DataAccessException("Invalid command parameters. Could not save to sqlite");
        }
    }

    /**
     * checks all of the parameters of the object to verify their validity
     * @param input the input object
     * @return PreparedStatement
     */
    @Override
    protected boolean checkParameters(User input) {
        return DataUtils.checkArgument(input) && DataUtils.checkArgument(input.getName()) && DataUtils.checkArgument(input.getPassword());
    }
}
