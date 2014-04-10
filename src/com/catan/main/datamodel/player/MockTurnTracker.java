package com.catan.main.datamodel.player;

import com.catan.main.datamodel.player.Player;
import com.catan.main.datamodel.player.Status;
import com.catan.main.datamodel.player.TurnTrackerInterface;

public class MockTurnTracker implements TurnTrackerInterface {

    //region Fields
    private static final int _players = 4;
    private Status status;
    private int currentTurn;
    //endregion

    public MockTurnTracker() {
        this.status = Status.FirstRound;
    }

    @Override
    public int getCurrentTurn() {
        return 0;
    }

    @Override
    public void setCurrentTurn(int currentTurn) {

    }

    @Override
    public boolean isLastPlayerTurn() {
        return false;
    }

    @Override
    public void advanceTurn() {

    }

    @Override
    public void reverseAdvanceTurn() {

    }

    @Override
    public Status getStatus() {
        return null;
    }

    @Override
    public void setStatus(Status status) {

    }

    @Override
    public boolean shouldDiscard(Player[] players) {
        return false;
    }
}
