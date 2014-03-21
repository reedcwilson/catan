package com.catan.main.datamodel.commands;

import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.hexgrid.vertex.VertexLocation;
import com.catan.main.datamodel.map.Map;
import com.catan.main.datamodel.message.MessageLine;
import com.catan.main.datamodel.player.Bank;
import com.catan.main.datamodel.player.Player;
import com.catan.main.datamodel.player.ResourceHand;

public class PlaceCity extends Command {

    //region Fields
    private VertexLocation location;
    //endregion

    public PlaceCity() {
    }

    //region Properties
    public VertexLocation getLocation() {
        return location;
    }
    public void setLocation(VertexLocation location) {
        this.location = location;
    }
    //endregion

    //region Methods
    public void setOwnerID(Integer ownerID) {
        this.setPlayerIndex(ownerID.intValue());
    }
    //endregion

    //region Overrides
    @Override
    public void doExecute(DataModel model) {
        Map map = model.getMap();
        Bank bank = model.getBank();
        map.addCity(this.location, this.getPlayerIndex());

        Player player = model.getPlayers()[this.getPlayerIndex()];
        if (model.getWinner().longValue() == -1L) {
            player.addVictoryPoint();
            if (player.wonGame()) {
                model.setWinner(player.getPlayerID());
            }
        }
        player.setCities(player.getCities() - 1);
        player.setSettlements(player.getSettlements() + 1);
        ResourceHand rh = new ResourceHand(0, 0, 0, 0, 0);
        rh.setWheat(-2);
        rh.setOre(-3);
        bank.giveResourcesToPlayer(player, rh);
    }
    @Override
    protected MessageLine getLogMessage(DataModel model) {
        String name = model.getPlayers()[model.getTurnTracker().getCurrentTurn()].getName();
        return new MessageLine(name, name + " upgraded to a city");
    }

    @Override
    public String toString() {
        return "PlaceCity{" +
                "location=" + location +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlaceCity)) return false;

        PlaceCity placeCity = (PlaceCity) o;

        if (location != null ? !location.equals(placeCity.location) : placeCity.location != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return location != null ? location.hashCode() : 0;
    }
    //endregion
}
