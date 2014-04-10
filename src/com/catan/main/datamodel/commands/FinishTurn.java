package com.catan.main.datamodel.commands;

import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.message.MessageLine;
import com.catan.main.datamodel.player.Player;
import com.catan.main.datamodel.player.Status;
import com.catan.main.datamodel.player.TurnTracker;
import com.catan.main.datamodel.player.TurnTrackerInterface;

public class FinishTurn extends Command {

    public FinishTurn() {
    }

    //region Methods
    private void setTrackerStatus(TurnTrackerInterface tracker) {
        if (tracker.getStatus() == Status.FirstRound) {
            if (tracker.isLastPlayerTurn()) {
                tracker.setStatus(Status.SecondRound);
            } else {
                tracker.advanceTurn();
            }
        } else if (tracker.getStatus() == Status.SecondRound) {
            if (tracker.getCurrentTurn() == 0) {
                tracker.setStatus(Status.Rolling);
            } else {
                tracker.reverseAdvanceTurn();
            }
        } else {
            tracker.advanceTurn();
            tracker.setStatus(Status.Rolling);
        }
    }
    //endregion

    //region Overrides
    @Override
    public void action(DataModel model) {
        TurnTrackerInterface tracker = model.getTurnTracker();
        if(model.getTurnTracker().getCurrentTurn() == this.getPlayerIndex())
        {
            Player[] players = model.getPlayers();
            Player pl = players[this.getPlayerIndex()];
            pl.getOldDevCards().addRange(pl.getNewDevCards());
            pl.getNewDevCards().removeRange(pl.getNewDevCards());
            pl.setPlayedDevCard(false);
            for (int i = 0; i < players.length; i++) {
                players[i].setDiscarded(false);
            }
            setTrackerStatus(tracker);
        }
    }
    @Override
    protected MessageLine getLog(DataModel model) {
        if(model.getTurnTracker().getCurrentTurn() == this.getPlayerIndex())
        {
            String name = model.getPlayers()[this.getPlayerIndex()].getName();
            return new MessageLine(name, name + "'s turn just ended");
        }
        else
        {
            return null;
        }
    }
    //endregion
}
