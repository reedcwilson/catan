package com.catan.main.datamodel.commands;

import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.hexgrid.hex.HexLocation;
import com.catan.main.datamodel.map.Map;
import com.catan.main.datamodel.message.MessageLine;
import com.catan.main.datamodel.player.Player;
import com.catan.main.datamodel.player.Status;
import com.catan.main.datamodel.player.TurnTracker;
import com.catan.main.datamodel.player.TurnTrackerInterface;

public class RobPlayer extends Command {

    //region Fields
    private int rand = -1;
    private int victimIndex;
    private HexLocation location;
    //endregion

    public RobPlayer() {
    }
    public RobPlayer(int victimIndex, HexLocation robberSpot, int rand) {
        this.victimIndex = victimIndex;
        this.location = robberSpot;
        this.rand = rand;
    }

    //region Properties
    public int getVictimIndex() {
        return this.victimIndex;
    }
    public void setVictimIndex(int victimIndex) {
        this.victimIndex = victimIndex;
    }

    public HexLocation getRobberSpot() {
        return this.location;
    }
    public void setRobberSpot(HexLocation robberSpot) {
        this.location = robberSpot;
    }
    //endregion

    //region Overrides
    @Override
    public void action(DataModel model) {
        TurnTrackerInterface t = model.getTurnTracker();
        Map m = model.getMap();
        if (this.victimIndex != -1) {
            Player[] players = model.getPlayers();


            Player attacker = players[this.getPlayerIndex()];
            Player victim = players[this.victimIndex];

            if(rand == -1)
                rand = (int) Math.floor(Math.random() * victim.getResources().total());
            attacker.rob(victim, rand);
        }
        m.setRobber(this.location);
        t.setStatus(Status.Playing);
    }
    @Override
    protected MessageLine getLog(DataModel model) {
        String robberName = model.getPlayerName(model.getTurnTracker().getCurrentTurn());
        String message = robberName + " moved the robber";
        String message2;
        if (this.victimIndex > -1) {
            String victimName = model.getPlayerName(this.victimIndex);
            message2 = " and robbed " + victimName + ".";
        } else {
            message2 = " but couldn't rob anyone!";
        }
        return new MessageLine(robberName, message + message2);
    }

    @Override
    public String toString() {
        return "RobPlayer{" +
                "victimIndex=" + victimIndex +
                ", location=" + location +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RobPlayer)) return false;

        RobPlayer robPlayer = (RobPlayer) o;

        if (victimIndex != robPlayer.victimIndex) return false;
        if (location != null ? !location.equals(robPlayer.location) : robPlayer.location != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = victimIndex;
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }
    //endregion
}
