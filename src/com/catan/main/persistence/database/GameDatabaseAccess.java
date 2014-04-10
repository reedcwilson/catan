package com.catan.main.persistence.database;

import com.catan.main.datamodel.game.Game;
import com.catan.main.persistence.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GameDatabaseAccess extends GameAccess<ResultSet, PreparedStatement> {

    private static final String singleSelectSql = "SELECT * FROM Game where id=?";
    private static final String selectSql = "SELECT * FROM Game";
    private static final String updateSql = "UPDATE Game SET commandIndex=?, currentBlob=?, originalBlob=? where id=?";
    private static final String insertSql = "INSERT INTO Game ('commandIndex', 'currentBlob', 'originalBlob') VALUES (?, ?, ?)";
    private static final String deleteSql = "DELETE FROM Game WHERE id=?";

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
    protected PreparedStatement getInsertStatement(Game input) throws DataAccessException {
        if (checkParameters(input)) {
            PreparedStatement stat = null;
            try {
                stat = dataContext.getConnection().prepareStatement(insertSql);
                // TODO: load the appropriate values into prepared statement
//                stat.setInt(1, input.getCommandId().intValue());
                stat.setBytes(2, DataUtils.serialize(input));
//                stat.setBytes(3, DataUtils.serialize(ServerUtils.getOriginalGame()));
            } catch (SQLException e) {
                DataUtils.crashOnException(e);
            }
            return stat;
        } else {
            throw new DataAccessException("Invalid command parameters. Could not save to database");
        }
    }

    /**
     * prepares the sql statement with the appropriate update parameters
     * @param input the input object
     * @return PreparedStatement
     * @throws DataAccessException
     */
    @Override
    protected PreparedStatement getUpdateStatement(Game input) throws DataAccessException {
        if (checkParameters(input)) {
            PreparedStatement stat = null;
            try {
                stat = dataContext.getConnection().prepareStatement(updateSql);
                // TODO: load the appropriate values into prepared statement
//                stat.setInt(1, input.getCommandId().intValue());
                stat.setBytes(2, DataUtils.serialize(input));
//                stat.setBytes(3, DataUtils.serialize(ServerUtils.getOriginalGame()));
                stat.setInt(4, input.getId().intValue());
            } catch (SQLException e) {
                DataUtils.crashOnException(e);
            }
            return stat;
        } else {
            throw new DataAccessException("Invalid command parameters. Could not save to database");
        }
    }

    /**
     * prepares the sql statement with the appropriate delete parameters
     * @param input the input object
     * @return PreparedStatement
     * @throws DataAccessException
     */
    @Override
    protected PreparedStatement getDeleteStatement(Game input) throws DataAccessException {
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
            throw new DataAccessException("Invalid command parameters. Could not save to database");
        }
    }

    /**
     * checks all of the parameters of the object to verify their validity
     * @param input the input object
     * @return PreparedStatement
     */
    @Override
    protected boolean checkParameters(Game input) {
        return DataUtils.checkArgument(input) && DataUtils.checkArgument(input.getId());
    }
}
