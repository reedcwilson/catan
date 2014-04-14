package com.catan.main.datamodel.hexgrid.hex;

import com.catan.main.datamodel.map.DesertHex;
import com.catan.main.datamodel.map.NormalHex;
import com.catan.main.datamodel.map.Resource;
import com.catan.main.datamodel.map.WaterHex;
import com.google.gson.*;

import java.lang.reflect.Type;

public class HexAdapter implements JsonDeserializer<Hex> {
    public Hex deserialize(JsonElement elem, Type interfaceType, JsonDeserializationContext context) throws JsonParseException {
        JsonObject json = ((JsonObject) elem);
        Gson gson = new Gson();
        if (json.get("landtype") != null) {
            return gson.fromJson(json.toString(), NormalHex.class);
//            return new NormalHex(getLocation(((JsonObject)json.get("location"))), Resource.valueOf(json.get("landtype").getAsString()));
        } else if (Boolean.parseBoolean(json.get("isLand").toString())) {
            return gson.fromJson(json.toString(), DesertHex.class);
//            return new DesertHex(getLocation(((JsonObject)json.get("location"))));
        } else {
            return gson.fromJson(json.toString(), WaterHex.class);
//            return new WaterHex(getLocation(((JsonObject)json.get("location"))));
        }
    }

    private HexLocation getLocation(JsonObject json) {
        return new HexLocation(json.get("x").getAsInt(), json.get("y").getAsInt());
    }
}
