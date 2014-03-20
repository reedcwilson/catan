package com.catan.main.datamodel.commands;

import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.message.MessageLine;
import com.catan.main.datamodel.player.Player;
import com.catan.main.datamodel.player.TradeOffer;

public class AcceptTrade extends Command {

    //region Fields
    private boolean accept;
    //endregion

    public AcceptTrade() {
    }

    //region Properties
    public boolean getAccept() {
        return this.accept;
    }
    public void setAccept(boolean accept) {
        this.accept = accept;
    }
    //endregion

    //region Overrides
    @Override
    public void doExecute(DataModel model) {
        if (this.accept) {
            TradeOffer t = model.getTradeOffer();
            Player[] players = model.getPlayers();
            Player sender = players[t.getSender()];
            Player receiver = players[t.getReceiver()];
            sender.getResources().removeRange(t.getOffer());
            receiver.getResources().addRange(t.getOffer());
        }
        model.setTradeOffer(null);
    }
    @Override
    protected MessageLine getLogMessage(DataModel model) {
        if (this.accept) {
            return new MessageLine(model.getPlayers()[0].getName(), "The trade was accepted");
        }
        return new MessageLine(model.getPlayers()[0].getName(), "The trade was not accepted");
    }
    //endregion
}
