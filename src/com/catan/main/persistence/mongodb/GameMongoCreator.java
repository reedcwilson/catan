package com.catan.main.persistence.mongodb;

import com.catan.main.datamodel.game.Game;
import com.catan.main.datamodel.hexgrid.hex.Hex;
import com.catan.main.datamodel.hexgrid.hex.HexAdapter;
import com.catan.main.datamodel.player.TurnTrackerAdapter;
import com.catan.main.datamodel.player.TurnTrackerInterface;
import com.catan.main.persistence.DataAccessException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class GameMongoCreator extends MongoCreator<Game> {
    @Override
    public Game initialize(DBCursor reader) throws DataAccessException {
        DBObject obj = reader.next();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeHierarchyAdapter(TurnTrackerInterface.class, new TurnTrackerAdapter()).create();
        gsonBuilder.registerTypeHierarchyAdapter(Hex.class, new HexAdapter());
        Gson gson = gsonBuilder.create();
        Game game = gson.fromJson(obj.get("currentBlob").toString(), Game.class);
        game.setId(Long.parseLong(obj.get("_id").toString()));
        return game;
    }
}
