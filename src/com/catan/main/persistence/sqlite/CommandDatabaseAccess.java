package com.catan.main.persistence.sqlite;

import com.catan.main.datamodel.commands.Command;
import com.catan.main.persistence.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CommandDatabaseAccess extends CommandAccess<PreparedStatement> {

    private static final String singleSelectSql = "SELECT * FROM Command where id=?";
    private static final String selectSql = "SELECT * FROM Command";
    private static final String updateSql = "UPDATE Command SET command=?, game_id=? where id=?";
    private static final String insertSql = "INSERT INTO Command ('command', 'game_id') VALUES (?, ?)";
    private static final String deleteSql = "DELETE FROM Command WHERE id=?";
    private static final String commandsForGameSelectSql = "SELECT * FROM Command WHERE game_id=?";


    private DatabaseContext dataContext;
    private CommandDatabaseCreator creator;

    public CommandDatabaseAccess(DatabaseContext dataContext) {
        this.dataContext = dataContext;
        creator = new CommandDatabaseCreator();
    }


    @Override
    public DataContext getDataContext() {
        return dataContext;
    }

    @Override
    public ObjectCreator<Command, ResultSet> getObjectCreator() {
        return creator;
    }

    /**
     * prepares the sql statement with the appropriate select parameters
     *
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
     *
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
     * prepares the sql statement with the appropriate select parameters
     *
     * @return PreparedStatement
     */
    @Override
    protected PreparedStatement getCommandsForGameStatement(Long gameId) throws DataAccessException {
        if (DataUtils.checkArgument(gameId)) {
            PreparedStatement stat = null;
            try {
                stat = dataContext.getConnection().prepareStatement(commandsForGameSelectSql);
                stat.setInt(1, gameId.intValue());
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
     *
     * @param input the input object
     * @return PreparedStatement
     * @throws DataAccessException
     */
    @Override
    protected PreparedStatement getInsertStatement(Command input) throws DataAccessException {
        if (checkParameters(input)) {
            PreparedStatement stat = null;
            try {
                stat = dataContext.getConnection().prepareStatement(insertSql);
                stat.setBytes(1, input.getBytes());
                stat.setInt(2, input.getGameId().intValue());
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
     *
     * @param input the input object
     * @return PreparedStatement
     * @throws DataAccessException
     */
    @Override
    protected PreparedStatement getUpdateStatement(Command input) throws DataAccessException {
        if (checkParameters(input)) {
            PreparedStatement stat = null;
            try {
                stat = dataContext.getConnection().prepareStatement(updateSql);
                stat.setBytes(1, input.getBytes());
                stat.setInt(2, input.getGameId().intValue());
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
     *
     * @param input the input object
     * @return PreparedStatement
     * @throws DataAccessException
     */
    @Override
    protected PreparedStatement getDeleteStatement(Command input) throws DataAccessException {
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
     *
     * @param input the input object
     * @return PreparedStatement
     */
    @Override
    protected boolean checkParameters(Command input) {
        return DataUtils.checkArgument(input);
    }
}
