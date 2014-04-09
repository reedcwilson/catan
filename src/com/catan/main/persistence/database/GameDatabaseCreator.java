package com.catan.main.persistence.database;

import com.catan.main.datamodel.game.Game;
import com.catan.main.persistence.DataAccessException;
import com.catan.main.persistence.ObjectCreator;

import java.sql.ResultSet;
import java.util.List;

public class GameDatabaseCreator implements ObjectCreator<Game, ResultSet> {
    @Override
    public Game initialize(ResultSet reader) throws DataAccessException {
        return null;
    }

    @Override
    public List<Game> initializeMany(ResultSet reader) throws DataAccessException {
        return null;
    }
}
