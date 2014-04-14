package com.catan.main.persistence.sqlite;

import com.catan.main.datamodel.game.Game;
import com.catan.main.persistence.DataAccessException;
import com.catan.main.persistence.DataUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GameDatabaseCreator extends DatabaseCreator<Game> {
    @Override
    public Game initialize(ResultSet reader) throws DataAccessException {
        try {
            Game game = (Game)DataUtils.deserialize(reader.getBytes(3));
            game.setCommandIndex(reader.getInt(2));
            game.setId((long)reader.getInt(1));
            return game;
        } catch (SQLException e) {
            DataUtils.crashOnException(e);
        }
        return null;
    }
}
