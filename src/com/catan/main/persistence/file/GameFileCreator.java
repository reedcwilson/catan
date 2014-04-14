package com.catan.main.persistence.file;

import com.catan.main.datamodel.game.Game;
import com.catan.main.persistence.DataAccessException;
import com.catan.main.persistence.DataUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

public class GameFileCreator extends FileCreator<Game> {
    @Override
    public Game initialize(File[] files) throws DataAccessException {
        try {
            byte[] bytes = DataUtils.readFromStream(new BufferedInputStream(new FileInputStream(files[0])));
            Game game =  (Game) DataUtils.deserialize(bytes);
            game.setId(Long.parseLong(files[0].getName()));
            return game;
        } catch (Exception e) {
            DataUtils.crashOnException(e);
        }
        return null;
    }
}
