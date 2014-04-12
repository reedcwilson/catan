package com.catan.main.persistence.sqlite;

import com.catan.main.datamodel.commands.Command;
import com.catan.main.datamodel.game.Game;
import com.catan.main.persistence.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GameDatabaseAccess extends GameAccess<PreparedStatement> {

    private static final String singleSelectSql = "SELECT * FROM Game where id=?";
    private static final String selectSql = "SELECT * FROM Game";
    private static final String updateSql = "UPDATE Game SET commandIndex=?, currentBlob=? where id=?";
    private static final String insertSql = "INSERT INTO Game ('commandIndex', 'currentBlob', 'originalblob') VALUES (?, ?, ?)";
    private static final String deleteSql = "DELETE FROM Game WHERE id=?";
    private static final String latestCommand = "SELECT * FROM Command WHERE game_id = ? ORDER BY id DESC LIMIT 1";
    private static final String selectWithUsers = "select * from game inner join usertogame on game.id = usertogame.game_id inner join user on usertogame.user_id = user.id where game.id = ?";

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
     * prepares the sql statement with the appropriate insert parameters
     *
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
                // get the latest command id
                stat.setInt(1, -1);
                stat.setBytes(2, DataUtils.serialize(input));
                stat.setBytes(3, DataUtils.serialize(input));
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
    protected PreparedStatement getUpdateStatement(Game input) throws DataAccessException {
        if (checkParameters(input)) {
            PreparedStatement stat = null;
            try {
                stat = dataContext.getConnection().prepareStatement(updateSql);
                // get the latest command id
                List<Command> commands = dataContext.get(getLatestCommand(input), dataContext.getCommandAccess().getObjectCreator());
                if (commands != null && commands.size() > 0) {
                    stat.setInt(1, commands.get(0).getId().intValue());
                } else {
                    stat.setInt(1, -1);
                }
                stat.setBytes(2, DataUtils.serialize(input));
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
    protected boolean checkParameters(Game input) {
        return DataUtils.checkArgument(input);
    }

    private PreparedStatement getLatestCommand(Game game) throws SQLException {
        if (game.getId() != null) {
            PreparedStatement stat;
            stat = dataContext.getConnection().prepareStatement(latestCommand);
            stat.setInt(1, game.getId().intValue());
            return stat;
        }
        return null;
    }
}
