package com.catan.main.datamodel.player;

import com.google.gson.*;

import java.lang.reflect.Type;

public class TurnTrackerAdapter implements JsonDeserializer<TurnTracker> {
    public TurnTracker deserialize(JsonElement elem, Type interfaceType, JsonDeserializationContext context) throws JsonParseException {
        JsonObject json = ((JsonObject)elem);

        return new TurnTracker(json.get("currentTurn").getAsInt(), Status.valueOf(json.get("status").getAsString()));
    }
}
