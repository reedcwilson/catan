package com.catan.main.datamodel.commands;

import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.map.Map;
import com.catan.main.datamodel.message.MessageLine;
import com.catan.main.datamodel.player.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RollDice extends Command {

    private static Logger _logger = Logger.getLogger(RollDice.class.getName());

    //region Fields
    private int number;
    //endregion

    public RollDice() {
    }
    public RollDice(int number) {
        this.number = number;
    }

    //region Properties
    public int getNumber() {
        return this.number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    //endregion

    //region Helper Methods
    private void allocateResources(DataModel model) {
        TurnTrackerInterface t = model.getTurnTracker();
        Player[] players = model.getPlayers();

        Map m = model.getMap();
        Bank b = model.getBank();
        t.setStatus(Status.Playing);
        ResourceHand[] rolledResources = m.getResourcesByRoll(Integer.valueOf(this.number));
        b.giveResourcesToPlayers(rolledResources, players);
    }
    private void startDiscarding(DataModel model) {
        TurnTrackerInterface t = model.getTurnTracker();
        Player[] players = model.getPlayers();
        if (t.shouldDiscard(players)) {
            t.setStatus(Status.Discarding);
        } else {
            t.setStatus(Status.Robbing);
        }
    }
    //endregion

    //region Overrides

    @Override
    public String toString() {
        return "RollDice{" +
                "number=" + number +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RollDice)) return false;

        RollDice rollDice = (RollDice) o;

        if (number != rollDice.number) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return number;
    }

    @Override
    protected MessageLine getLog(DataModel model) {
        String name = model.getPlayerName(model.getTurnTracker().getCurrentTurn());
        return new MessageLine(name, name + " rolled a " + this.number + ".");
    }
    @Override
    public void action(DataModel model) {
        _logger.log(Level.FINER, "roll execute:   You rolled a " + this.number);
        if (this.number != 7) {
            allocateResources(model);
        } else {
            startDiscarding(model);
        }
    }
    //endregion
}
