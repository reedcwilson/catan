package com.catan.main.persistence.database;

import com.catan.main.datamodel.game.Game;
import com.catan.main.persistence.DataAccessException;

import java.sql.ResultSet;

public class GameDatabaseCreator extends DatabaseCreator<Game> {
    @Override
    public Game initialize(ResultSet reader) throws DataAccessException {
        return null;
    }
}
