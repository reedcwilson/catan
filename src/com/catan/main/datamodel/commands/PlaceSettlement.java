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
    private VertexLocation vertexLocation;
    private Boolean free;
    //endregion

    public PlaceSettlement() {
    }
    public PlaceSettlement(VertexLocation vertexLocation, Integer ownerID, Boolean free) {
        this.vertexLocation = vertexLocation;
        this.free = free;
        setPlayerIndex(ownerID.intValue());
    }

    //region Properties
    public VertexLocation getVertexLocation() {
        return vertexLocation;
    }
    public void setVertexLocation(VertexLocation vertexLocation) {
        this.vertexLocation = vertexLocation;
    }

    public Boolean getFree() {
        return free;
    }
    public void setFree(Boolean free) {
        this.free = free;
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
                "location=" + vertexLocation +
                ", available=" + free +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlaceSettlement)) return false;

        PlaceSettlement that = (PlaceSettlement) o;

        if (free != null ? !free.equals(that.free) : that.free != null) return false;
        if (vertexLocation != null ? !vertexLocation.equals(that.vertexLocation) : that.vertexLocation != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = vertexLocation != null ? vertexLocation.hashCode() : 0;
        result = 31 * result + (free != null ? free.hashCode() : 0);
        return result;
    }

    @Override
    public void action(DataModel model) {
        Map map = model.getMap();
        TurnTrackerInterface tracker = model.getTurnTracker();
        Bank bank = model.getBank();
        map.addSettlement(this.vertexLocation, this.getPlayerIndex());

        Player player = model.getPlayers()[this.getPlayerIndex()];
        if (model.getWinner().longValue() == -1L) {
            player.addVictoryPoint();
            if (player.wonGame()) {
                model.setWinner(player.getPlayerID());
            }
        }
        player.setSettlements(player.getSettlements() - 1);
        if (!this.free.booleanValue()) {
            ResourceHand rh = new ResourceHand(0,0,0,0,0);
            rh.setWood(-1);
            rh.setBrick(-1);
            rh.setWheat(-1);
            rh.setSheep(-1);
            bank.giveResourcesToPlayer(model.getPlayers()[this.getPlayerIndex()], rh);
        }
        if (tracker.getStatus() == Status.SecondRound) {
            ArrayList<Resource> resources = map.getLandTypeOfSurrounding(this.vertexLocation);
            ResourceHand rh = new ResourceHand(0,0,0,0,0);
            for (Resource res : resources) {
                rh.set(res, 1);
            }
            bank.giveResourcesToPlayer(model.getPlayers()[this.getPlayerIndex()], rh);
        }
    }

    @Override
    protected MessageLine getLog(DataModel model) {
        String name = model.getPlayers()[this.getPlayerIndex()].getName();
        return new MessageLine(name, name + " built a settlement");
    }

    //endregion
}
