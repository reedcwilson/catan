package com.catan.main.datamodel.commands;

import com.google.gson.*;

import java.lang.reflect.Type;

public class CommandAdapter implements JsonDeserializer<Command> {
    public Command deserialize(JsonElement elem, Type interfaceType, JsonDeserializationContext context) throws JsonParseException {
        JsonObject json = ((JsonObject) elem);
        return getCommand(json);
    }

    private Command getCommand(JsonObject json) {
        Gson gson = new Gson();
        switch (commandType.valueOf(json.get("type").getAsString())) {

            case sendChat:
                return gson.fromJson(json.toString(), SendChat.class);
            case acceptTrade:
                return gson.fromJson(json.toString(), AcceptTrade.class);
            case discardCards:
                return gson.fromJson(json.toString(), Discard.class);
            case rollNumber:
                return gson.fromJson(json.toString(), RollDice.class);
            case buildRoad:
                return gson.fromJson(json.toString(), PlaceRoad.class);
            case buildSettlement:
                return gson.fromJson(json.toString(), PlaceSettlement.class);
            case buildCity:
                return gson.fromJson(json.toString(), PlaceCity.class);
            case offerTrade:
                return gson.fromJson(json.toString(), OfferTrade.class);
            case maritimeTrade:
                return gson.fromJson(json.toString(), MaritimeTrade.class);
            case finishTurn:
                return gson.fromJson(json.toString(), FinishTurn.class);
            case buyDevCard:
                return gson.fromJson(json.toString(), BuyDevCard.class);
            case Year_of_Plenty:
                return gson.fromJson(json.toString(), YearOfPlenty.class);
            case Road_Building:
                return gson.fromJson(json.toString(), RoadBuilding.class);
            case Soldier:
                return gson.fromJson(json.toString(), Soldier.class);
            case Monopoly:
                return gson.fromJson(json.toString(), Monopoly.class);
            case Monument:
                return gson.fromJson(json.toString(), Monument.class);
            default:
                throw new IllegalArgumentException("Unrecognized command type: " + commandType.valueOf(json.get("type").getAsString()));
        }
    }

    enum commandType {
        sendChat,
        acceptTrade,
        discardCards,
        rollNumber,
        buildRoad,
        buildSettlement,
        buildCity,
        offerTrade,
        maritimeTrade,
        finishTurn,
        buyDevCard,
        Year_of_Plenty,
        Road_Building,
        Soldier,
        Monopoly,
        Monument
    }
}
