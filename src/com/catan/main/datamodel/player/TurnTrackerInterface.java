package com.catan.main.datamodel.player;

/**
 * Created by jakeparkinson on 3/29/14.
 */
public interface TurnTrackerInterface {
    public int getCurrentTurn();
    public void setCurrentTurn(int currentTurn);
    public boolean isLastPlayerTurn();
    public void advanceTurn();
    public void reverseAdvanceTurn();
    public Status getStatus();
    public void setStatus(Status status);
    public boolean shouldDiscard(Player[] players);
}
