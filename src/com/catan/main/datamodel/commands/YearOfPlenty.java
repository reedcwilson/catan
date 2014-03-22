package com.catan.main.datamodel.commands;

import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.devcard.DevCardType;
import com.catan.main.datamodel.map.Resource;
import com.catan.main.datamodel.message.MessageLine;

public class YearOfPlenty extends DevCard {

    //region Fields
    private Resource resource1;
    private Resource resource2;
    //endregion

    public YearOfPlenty(Resource resource1, Resource resource2) {
        this();
        this.resource1 = resource1;
        this.resource2 = resource2;
    }
    public YearOfPlenty() {
        super(DevCardType.Year_of_Plenty);
    }

    //region Properties
    public Resource getResource1() {
        return this.resource1;
    }
    public void setResource1(Resource resource1) {
        this.resource1 = resource1;
    }

    public Resource getResource2() {
        return this.resource2;
    }
    public void setResource2(Resource resource2) {
        this.resource2 = resource2;
    }
    //endregion

    //region Overrides
    @Override
    protected MessageLine getLog(DataModel model) {
        String name = model.getPlayerName(this.getPlayerIndex());
        return new MessageLine(name, name + " used Year of Plenty and got a " + this.resource1.toString().toLowerCase() + " and a " + this.resource2.toString().toLowerCase());
    }

    @Override
    protected void playDevCard(DataModel model) {
        model.getPlayers()[this.getPlayerIndex()].getResources().add(this.resource1, 1);
        model.getPlayers()[this.getPlayerIndex()].getResources().add(this.resource2, 1);
        model.getPlayers()[this.getPlayerIndex()].setPlayedDevCard(true);
    }

    @Override
    public String toString() {
        return "YearOfPlenty{" +
                "resource1=" + resource1 +
                ", resource2=" + resource2 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YearOfPlenty)) return false;

        YearOfPlenty that = (YearOfPlenty) o;

        if (resource1 != that.resource1) return false;
        if (resource2 != that.resource2) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = resource1 != null ? resource1.hashCode() : 0;
        result = 31 * result + (resource2 != null ? resource2.hashCode() : 0);
        return result;
    }
    //endregion
}
