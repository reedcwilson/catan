package com.catan.main.datamodel.commands;

import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.devcard.DevCardType;
import com.catan.main.datamodel.hexgrid.edge.EdgeLocation;
import com.catan.main.datamodel.message.MessageLine;

public class RoadBuilding extends DevCard {

    //region Fields
    private EdgeLocation location1;
    private EdgeLocation location2;
    //endregion

    public RoadBuilding(EdgeLocation location1, EdgeLocation location2) {
        this();
        this.location1 = location1;
        this.location2 = location2;
    }
    public RoadBuilding() {
        super(DevCardType.Road_Building);
    }

    //region Properties
    public EdgeLocation getLocation1() {
        return this.location1;
    }
    public void setLocation1(EdgeLocation location1) {
        this.location1 = location1;
    }

    public EdgeLocation getLocation2() {
        return this.location2;
    }
    public void setLocation2(EdgeLocation location2) {
        this.location2 = location2;
    }
    //endregion

    //region Overrides
    @Override
    protected void playDevCard(DataModel model) {
        new PlaceRoad(this.location1, Integer.valueOf(this.getPlayerIndex()), Boolean.valueOf(true)).execute(model);
        new PlaceRoad(this.location2, Integer.valueOf(this.getPlayerIndex()), Boolean.valueOf(true)).execute(model);
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
                "location1=" + location1 +
                ", location2=" + location2 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoadBuilding)) return false;

        RoadBuilding that = (RoadBuilding) o;

        if (location1 != null ? !location1.equals(that.location1) : that.location1 != null) return false;
        if (location2 != null ? !location2.equals(that.location2) : that.location2 != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = location1 != null ? location1.hashCode() : 0;
        result = 31 * result + (location2 != null ? location2.hashCode() : 0);
        return result;
    }
    //endregion
}