package com.catan.main.persistence;

import com.catan.main.datamodel.game.Game;

public abstract class GameAccess implements IAccess<Game> {
    // TODO: we will cache all games here and only go to data store when we have an old copy
}
