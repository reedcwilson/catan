package com.catan.main.datamodel.commands;

import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.hexgrid.edge.EdgeLocation;
import com.catan.main.datamodel.map.Map;
import com.catan.main.datamodel.message.MessageLine;
import com.catan.main.datamodel.player.Bank;
import com.catan.main.datamodel.player.Player;
import com.catan.main.datamodel.player.ResourceHand;

public class PlaceRoad extends Command {

    //region Fields
    private EdgeLocation location;
    private boolean available;
    //endregion

    public PlaceRoad() {
    }
    public PlaceRoad(EdgeLocation roadLocation, Integer playerIndex, Boolean available) {
        this.location = roadLocation;
        this.available = available.booleanValue();
        setPlayerIndex(playerIndex.intValue());
    }

    //region Properties
    public EdgeLocation getLocation() {
        return this.location;
    }
    public void setLocation(EdgeLocation location) {
        this.location = location;
    }

    public boolean isAvailable() {
        return available;
    }
    public void setAvailable(Boolean available) {
        this.available = available.booleanValue();
    }
    //endregion

    //region Methods
    public void setPlayerIndex(Integer playerIndex) {
        this.setPlayerIndex(playerIndex.intValue());
    }
    //endregion

    //region Overrides
    @Override
    public void action(DataModel model) {
        Map map = model.getMap();
        Bank bank = model.getBank();

        map.addRoad(this.location, this.getPlayerIndex());
        // TODO: modify most roads person
        Player player = model.getPlayers()[this.getPlayerIndex()];
        if ((model.getWinner().longValue() == -1L) &&
                (player.wonGame())) {
            model.setWinner(player.getPlayerID());
        }
        player.setRoads(player.getRoads() - 1);
        if (!this.available) {
            ResourceHand rh = new ResourceHand();
            rh.setWood(-1);
            rh.setBrick(-1);
            bank.giveResourcesToPlayer(model.getPlayers()[this.getPlayerIndex()], rh);
        }
    }

    @Override
    protected MessageLine getLog(DataModel model) {
        String name = model.getPlayers()[this.getPlayerIndex()].getName();
        return new MessageLine(name, name + " built a road");
    }

    @Override
    public String toString() {
        return "PlaceRoad{" +
                "location=" + location +
                ", available=" + available +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlaceRoad)) return false;

        PlaceRoad placeRoad = (PlaceRoad) o;

        if (available != placeRoad.available) return false;
        if (location != null ? !location.equals(placeRoad.location) : placeRoad.location != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = location != null ? location.hashCode() : 0;
        result = 31 * result + (available ? 1 : 0);
        return result;
    }
    //endregion
}
