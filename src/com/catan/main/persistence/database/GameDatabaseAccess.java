package com.catan.main.persistence.database;

import com.catan.main.datamodel.game.Game;
import com.catan.main.persistence.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GameDatabaseAccess extends GameAccess<ResultSet, PreparedStatement> {

    private DatabaseContext dataContext;
    private GameDatabaseCreator creator;

    public GameDatabaseAccess(DatabaseContext dataContext) {
        this.dataContext = dataContext;
        creator = new GameDatabaseCreator();
    }

    @Override
    public DataContext getDataContext() {
        return dataContext;
    }

    @Override
    public ObjectCreator<Game, ResultSet> getObjectCreator() {
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
    protected PreparedStatement getInsertStatement(Game input)
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
    protected PreparedStatement getUpdateStatement(Game input)
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
    protected PreparedStatement getDeleteStatement(Game input)
            throws DataAccessException {
        return null;
    }

    /**
     * checks all of the parameters of the object to verify their validity
     * @param input the input object
     * @return PreparedStatement
     */
    @Override
    protected boolean checkParameters(Game input) {
        return false;
    }
}
