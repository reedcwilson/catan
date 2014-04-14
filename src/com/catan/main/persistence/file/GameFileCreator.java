package com.catan.main.persistence.file;

import com.catan.main.datamodel.game.Game;
import com.catan.main.persistence.DataAccessException;

import java.io.File;

public class GameFileCreator extends FileCreator<Game> {
    @Override
    public Game initialize(File[] files) throws DataAccessException {
        // TODO: deserialize game
        return null;
    }
}
