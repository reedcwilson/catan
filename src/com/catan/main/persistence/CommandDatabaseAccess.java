package com.catan.main.persistence;

import com.catan.main.datamodel.commands.Command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CommandDatabaseAccess extends DatabaseAccess<Command> {

    public CommandDatabaseAccess(DatabaseContext dataContext) {
        super(dataContext);
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
     * @throws DataAccessException
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
    protected PreparedStatement getInsertStatement(Command input)
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
    protected PreparedStatement getUpdateStatement(Command input)
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
    protected PreparedStatement getDeleteStatement(Command input)
            throws DataAccessException {
        return null;
    }

    /**
     * checks all of the parameters of the object to verify their validity
     * @param input the input object
     * @return PreparedStatement
     */
    @Override
    protected boolean checkParameters(Command input) {
        return false;
    }

    /**
     * initializes an object of type T with given resultSet
     * @param reader the reader
     * @param list the list
     * @return T
     * @throws DataAccessException
     * @throws SQLException
     */
    @Override
    protected Command initialize(ResultSet reader, List<Command> list)
            throws DataAccessException, SQLException {
        return null;
    }
}
