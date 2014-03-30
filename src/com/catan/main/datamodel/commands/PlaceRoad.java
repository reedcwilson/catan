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
    private EdgeLocation roadLocation;
    private boolean free;
    //endregion

    public PlaceRoad(EdgeLocation roadLocation, Integer playerIndex, Boolean available) {
        this.roadLocation = roadLocation;
        this.free = available.booleanValue();
        setPlayerIndex(playerIndex.intValue());
    }

    //region Properties
    public EdgeLocation getRoadLocation() {
        return this.roadLocation;
    }
    public void setRoadLocation(EdgeLocation roadLocation) {
        this.roadLocation = roadLocation;
    }

    public boolean isFree() {
        return free;
    }
    public void setFree(Boolean free) {
        this.free = free.booleanValue();
    }
    //endregion

    //region Methods
    public void setPlayerIndex(Integer playerIndex) {
        this.setPlayerIndex(playerIndex.intValue());
    }
    private long getPlayerWithMostRoads(DataModel model) {
        Player[] players = model.getPlayers();
        Player playerWithMostRoads = players[0];
        for (Player player : players) {
            if (!player.equals(playerWithMostRoads) && player.getRoads() < playerWithMostRoads.getRoads()) {
                playerWithMostRoads = player;
            }
        }
        if (playerWithMostRoads.getRoads() < 11)
            return playerWithMostRoads.getPlayerID();
        return -1L;
    }
    //endregion

    //region Overrides
    @Override
    public void action(DataModel model) {
        Map map = model.getMap();
        Bank bank = model.getBank();

        map.addRoad(this.roadLocation, this.getPlayerIndex());
        // TODO: add really cool symbols all over our files ♦♥♣♠
        Player player = model.getPlayers()[this.getPlayerIndex()];
        player.setRoads(player.getRoads() - 1);
        if (!this.free) {
            ResourceHand rh = new ResourceHand();
            rh.setWood(-1);
            rh.setBrick(-1);
            bank.giveResourcesToPlayer(model.getPlayers()[this.getPlayerIndex()], rh);
        }
        long mostRoadsPlayerIdx = getPlayerWithMostRoads(model);
        if (mostRoadsPlayerIdx != -1) {
            model.setLongestRoad((int)mostRoadsPlayerIdx);
        }
        if ((model.getWinner() == -1L) && (player.wonGame())) {
            model.setWinner(player.getPlayerID());
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
                "location=" + roadLocation +
                ", available=" + free +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlaceRoad)) return false;

        PlaceRoad placeRoad = (PlaceRoad) o;

        if (free != placeRoad.free) return false;
        if (roadLocation != null ? !roadLocation.equals(placeRoad.roadLocation) : placeRoad.roadLocation != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = roadLocation != null ? roadLocation.hashCode() : 0;
        result = 31 * result + (free ? 1 : 0);
        return result;
    }
    //endregion
}
