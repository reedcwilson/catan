package com.catan.main.datamodel.commands;

import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.devcard.DevCardType;
import com.catan.main.datamodel.hexgrid.edge.EdgeLocation;
import com.catan.main.datamodel.message.MessageLine;
import com.catan.main.persistence.DataAccessException;

public class RoadBuilding extends DevCard {

    //region Fields
    private EdgeLocation spot1;
    private EdgeLocation spot2;
    //endregion

    public RoadBuilding(EdgeLocation location1, EdgeLocation location2) {
        this();
        this.spot1 = location1;
        this.spot2 = location2;
    }
    public RoadBuilding() {
        super(DevCardType.Road_Building);
    }

    //region Properties
    public EdgeLocation getSpot1() {
        return this.spot1;
    }
    public void setSpot1(EdgeLocation spot1) {
        this.spot1 = spot1;
    }

    public EdgeLocation getSpot2() {
        return this.spot2;
    }
    public void setSpot2(EdgeLocation spot2) {
        this.spot2 = spot2;
    }
    //endregion

    //region Overrides
    @Override
    protected void playDevCard(DataModel model) throws DataAccessException {
        new PlaceRoad(this.spot1, Integer.valueOf(this.getPlayerIndex()), Boolean.valueOf(true)).execute(model);
        new PlaceRoad(this.spot2, Integer.valueOf(this.getPlayerIndex()), Boolean.valueOf(true)).execute(model);
        model.getPlayers()[this.getPlayerIndex()].setPlayedDevCard(true);
    }
    @Override
    protected MessageLine getLog(DataModel model) {
        String name = model.getPlayerName(this.getPlayerIndex());
        return new MessageLine(name, name + " built 2 roads");
    }

    @Override
    public String toString() {
        return "RoadBuilding{" +
                "location1=" + spot1 +
                ", location2=" + spot2 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoadBuilding)) return false;

        RoadBuilding that = (RoadBuilding) o;

        if (spot1 != null ? !spot1.equals(that.spot1) : that.spot1 != null) return false;
        if (spot2 != null ? !spot2.equals(that.spot2) : that.spot2 != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = spot1 != null ? spot1.hashCode() : 0;
        result = 31 * result + (spot2 != null ? spot2.hashCode() : 0);
        return result;
    }
    //endregion
}