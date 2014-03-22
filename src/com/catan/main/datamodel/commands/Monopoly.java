package com.catan.main.datamodel.commands;

import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.devcard.DevCardType;
import com.catan.main.datamodel.map.Resource;
import com.catan.main.datamodel.message.MessageLine;
import com.catan.main.datamodel.player.Player;

public class Monopoly extends DevCard {

    //region Fields
    Resource resource;
    //endregion

    public Monopoly(Resource resource) {
        this();
        this.resource = resource;
    }
    public Monopoly() {
        super(DevCardType.Monopoly);
    }

    //region Properties
    public Resource getResource() {
        return this.resource;
    }
    public void setResource(Resource resource) {
        this.resource = resource;
    }
    //endregion

    //region Overrides
    @Override
    protected void playDevCard(DataModel model) {
        Player[] players = model.getPlayers();
        Player robber = players[this.getPlayerIndex()];
        for (int count = 0; count < 4; count++) {
            if (count != this.getPlayerIndex()) {
                int rCount = players[count].getResources().get(this.resource);
                players[count].getResources().set(this.resource, 0);
                robber.getResources().add(this.resource, rCount);
                robber.setPlayedDevCard(true);
            }
        }
    }
    @Override
    protected MessageLine getLog(DataModel model) {
        String name = model.getPlayerName(this.getPlayerIndex());
        return new MessageLine(name, name + " stole everyones " + this.resource.toString().toLowerCase());
    }

    @Override
    public String toString() {
        return "Monopoly{" +
                "resource=" + resource +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Monopoly)) return false;

        Monopoly monopoly = (Monopoly) o;

        if (resource != monopoly.resource) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return resource != null ? resource.hashCode() : 0;
    }
    //endregion
}
