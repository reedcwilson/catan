package com.catan.main.datamodel.commands;

import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.message.MessageLine;
import com.catan.main.datamodel.player.Player;
import com.catan.main.datamodel.player.Status;
import com.catan.main.datamodel.player.TurnTracker;

public class FinishTurn extends Command {

    public FinishTurn() {
    }

    //region Methods
    private void setTrackerStatus(TurnTracker tracker) {
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
        TurnTracker tracker = model.getTurnTracker();

        String name = model.getPlayers()[model.getTurnTracker().getCurrentTurn()].getName();
        MessageLine mline = new MessageLine(name, name + "'s turn just ended");
        model.getLog().addLine(mline);
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
    @Override
    protected MessageLine getLog(DataModel model) {
        //String name = model.getPlayers()[model.getTurnTracker().getCurrentTurn()].getName();
        //return new MessageLine(name, name + "'s turn just ended");
        return null;
    }
    //endregion
}
