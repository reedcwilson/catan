package com.catan.main.datamodel.player;

import java.io.Serializable;

public class TurnTracker implements TurnTrackerInterface, Serializable {

    //region Fields
    private static final int _players = 4;
    private Status status;
    private int currentTurn;
    //endregion

    public TurnTracker() {
        this.status = Status.FirstRound;
    }
    public TurnTracker(int turn, Status status) {
        this.status = status;
        this.currentTurn = turn;
    }

    //region Public Interface

    //region Turns
    public int getCurrentTurn() {
        return this.currentTurn;
    }
    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }
    public boolean isLastPlayerTurn() {
        return this.currentTurn == _players - 1;
    }
    public void advanceTurn() {
        this.currentTurn = ((this.currentTurn + 1) % _players);
    }
    public void reverseAdvanceTurn() {
        this.currentTurn = (Math.abs(this.currentTurn - 1) % _players);
    }
    //endregion

    //region Status
    public Status getStatus() {
        return this.status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    //endregion

    //region Discard
    public boolean shouldDiscard(Player[] players) {
        for (int i = 0; i < players.length; i++) {
            if ((players[i].getResources().total() > 7) && (!players[i].hasDiscarded())) {
                return true;
            }
        }
        return false;
    }
    //endregion

    //endregion

}
