package com.catan.main.datamodel.player;

public class TurnTracker {

    //region Fields
    private final int _players = 4;
    private Status _status;
    private int _currentTurn;
    //endregion

    public TurnTracker() {
        this._status = Status.FirstRound;
    }
    public TurnTracker(int turn, Status status) {
        this._status = status;
        this._currentTurn = turn;
    }

    //region Public Interface

    //region Turns
    public int getCurrentTurn() {
        return this._currentTurn;
    }
    public void setCurrentTurn(int currentTurn) {
        this._currentTurn = currentTurn;
    }
    public boolean isLastPlayerTurn() {
        return this._currentTurn == _players - 1;
    }
    public void advanceTurn() {
        this._currentTurn = ((this._currentTurn + 1) % _players);
    }
    public void reverseAdvanceTurn() {
        this._currentTurn = (Math.abs(this._currentTurn - 1) % _players);
    }
    //endregion

    //region Status
    public Status getStatus() {
        return this._status;
    }
    public void setStatus(Status status) {
        this._status = status;
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
