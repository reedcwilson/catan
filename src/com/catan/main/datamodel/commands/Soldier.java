package com.catan.main.datamodel.commands;

import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.devcard.DevCardType;
import com.catan.main.datamodel.hexgrid.hex.HexLocation;
import com.catan.main.datamodel.message.MessageLine;
import com.catan.main.datamodel.player.Player;
import com.catan.main.persistence.DataAccessException;

public class Soldier extends DevCard {

    //region Fields
    private HexLocation location;
    private Integer victimIndex;
    private int rand = -1;
    //endregion

    public Soldier() {
        super(DevCardType.Soldier);
    }
    public Soldier(HexLocation location, Integer victimID) {
        this();
        this.location = location;
        this.victimIndex = victimID;
    }

    //region Properties
    public HexLocation getLocation() {
        return this.location;
    }
    public void setLocation(HexLocation location) {
        this.location = location;
    }

    public Integer getVictimID() {
        return this.victimIndex;
    }
    public void setVictimID(Integer victimID) {
        this.victimIndex = victimID;
    }
    //endregion

    //region Overrides
    @Override
    protected void playDevCard(DataModel model) throws DataAccessException {
        int currentBiggest = model.getBiggestArmy();
        int playerArmy = model.getPlayers()[this.getPlayerIndex()].getSoldiers();
        Player currentPlayer = model.getPlayers()[this.getPlayerIndex()];

        playerArmy++;
        currentPlayer.setSoldiers(playerArmy);
        currentPlayer.setPlayedDevCard(true);
        if (playerArmy > currentBiggest) {
            Player[] players = model.getPlayers();
            for (int i = 0; i < players.length; i++) {
                players[i].setLargestArmy(players[i].equals(currentPlayer));
            }
            model.setBiggestArmy(playerArmy);
        }
        if ((model.getWinner().longValue() == -1L) && (currentPlayer.wonGame())) {
            model.setWinner(currentPlayer.getPlayerID());
        }
        Player victim = model.getPlayers()[this.victimIndex];
        if(rand == -1)
            rand = (int) Math.floor(Math.random() * victim.getResources().total());
        new RobPlayer(this.victimIndex.intValue(), this.location, rand).execute(model);
    }
    @Override
    protected MessageLine getLog(DataModel model) {
        String name = model.getPlayerName(model.currentTurn());
        String addendum = "";
        if (model.getPlayers()[model.currentTurn()].getLargestArmy()) {
            addendum = "and gained the biggest army";
        }
        return new MessageLine(name, name + " used a soldier " + addendum);
    }

    @Override
    public String toString() {
        return "Soldier{" +
                "location=" + location +
                ", victim=" + victimIndex +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Soldier)) return false;

        Soldier soldier = (Soldier) o;

        if (location != null ? !location.equals(soldier.location) : soldier.location != null) return false;
        if (victimIndex != null ? !victimIndex.equals(soldier.victimIndex) : soldier.victimIndex != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = location != null ? location.hashCode() : 0;
        result = 31 * result + (victimIndex != null ? victimIndex.hashCode() : 0);
        return result;
    }
    //endregion
}
