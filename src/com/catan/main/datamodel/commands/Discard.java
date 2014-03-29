package com.catan.main.datamodel.commands;

import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.message.MessageLine;
import com.catan.main.datamodel.player.*;

public class Discard extends Command {

    //region Fields
    private ResourceHand discardedCards;
    //endregion

    public Discard() {
    }
    public Discard(int playerIndex, ResourceHand discardCards) {
        this.setPlayerIndex(playerIndex);
        this.discardedCards = discardCards;
    }

    //region Properties
    public ResourceHand getDiscardedCards() {
        return this.discardedCards;
    }
    public void setDiscardedCards(ResourceHand discardedCards) {
        this.discardedCards = discardedCards;
    }

    public int getPlayerIndex() {
        return super.getPlayerIndex();
    }
    public void setPlayerIndex(int playerIndex) {
        this.setPlayerIndex(playerIndex);
    }
    //endregion

    //region Overrides
    @Override
    public void action(DataModel model) {
        TurnTrackerInterface t = model.getTurnTracker();
        Player[] players = model.getPlayers();

        players[this.getPlayerIndex()].getResources().removeRange(this.discardedCards);
        players[this.getPlayerIndex()].setDiscarded(true);
        model.getBank().addRange(this.discardedCards);
        if (t.shouldDiscard(players)) {
            t.setStatus(Status.Discarding);
        } else {
            t.setStatus(Status.Robbing);
        }
    }
    @Override
    protected MessageLine getLog(DataModel model) {
        return null;
    }

    @Override
    public String toString() {
        return "Discard{" +
                "discardedCards=" + discardedCards +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Discard)) return false;

        Discard discard = (Discard) o;

        if (discardedCards != null ? !discardedCards.equals(discard.discardedCards) : discard.discardedCards != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return discardedCards != null ? discardedCards.hashCode() : 0;
    }
    //endregion
}
