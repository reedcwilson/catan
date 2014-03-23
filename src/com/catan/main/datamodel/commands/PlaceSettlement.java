package com.catan.main.datamodel.commands;

import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.hexgrid.vertex.VertexLocation;
import com.catan.main.datamodel.map.Map;
import com.catan.main.datamodel.map.Resource;
import com.catan.main.datamodel.message.MessageLine;
import com.catan.main.datamodel.player.*;

import java.util.ArrayList;

public class PlaceSettlement extends Command {

    //region Fields
    private VertexLocation location;
    private Boolean available;
    //endregion

    public PlaceSettlement() {
    }
    public PlaceSettlement(VertexLocation vertexLocation, Integer ownerID, Boolean free) {
        this.location = vertexLocation;
        this.available = free;
        setPlayerIndex(ownerID.intValue());
    }

    //region Properties
    public VertexLocation getLocation() {
        return location;
    }
    public void setLocation(VertexLocation location) {
        this.location = location;
    }

    public Boolean getAvailable() {
        return available;
    }
    public void setAvailable(Boolean available) {
        this.available = available;
    }
    //endregion

    //region Methods
    public void setOwnerID(Integer ownerID) {
        this.setPlayerIndex(ownerID.intValue());
    }
    //endregion

    //region Overrides

    @Override
    public String toString() {
        return "PlaceSettlement{" +
                "location=" + location +
                ", available=" + available +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlaceSettlement)) return false;

        PlaceSettlement that = (PlaceSettlement) o;

        if (available != null ? !available.equals(that.available) : that.available != null) return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = location != null ? location.hashCode() : 0;
        result = 31 * result + (available != null ? available.hashCode() : 0);
        return result;
    }

    @Override
    public void action(DataModel model) {
        Map map = model.getMap();
        TurnTracker tracker = model.getTurnTracker();
        Bank bank = model.getBank();
        map.addSettlement(this.location, this.getPlayerIndex());

        Player player = model.getPlayers()[this.getPlayerIndex()];
        if (model.getWinner().longValue() == -1L) {
            player.addVictoryPoint();
            if (player.wonGame()) {
                model.setWinner(player.getPlayerID());
            }
        }
        player.setSettlements(player.getSettlements() - 1);
        if (!this.available.booleanValue()) {
            ResourceHand rh = new ResourceHand(0,0,0,0,0);
            rh.setWood(-1);
            rh.setBrick(-1);
            rh.setWheat(-1);
            rh.setSheep(-1);
            bank.giveResourcesToPlayer(model.getPlayers()[this.getPlayerIndex()], rh);
        }
        if (tracker.getStatus() == Status.SecondRound) {
            ArrayList<Resource> resources = map.getLandTypeOfSurrounding(this.location);
            ResourceHand rh = new ResourceHand(0,0,0,0,0);
            for (Resource res : resources) {
                rh.set(res, 1);
            }
            bank.giveResourcesToPlayer(model.getPlayers()[this.getPlayerIndex()], rh);
        }
    }

    @Override
    protected MessageLine getLog(DataModel model) {
        String name = model.getPlayers()[model.getTurnTracker().getCurrentTurn()].getName();
        return new MessageLine(name, name + " built a settlement");
    }

    //endregion
}
