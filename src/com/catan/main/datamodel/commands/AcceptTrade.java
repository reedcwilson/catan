package com.catan.main.datamodel.commands;

import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.message.MessageLine;
import com.catan.main.datamodel.player.Player;
import com.catan.main.datamodel.player.TradeOffer;

public class AcceptTrade extends Command {

    //region Fields
    private boolean willAccept;
    //endregion

    public AcceptTrade() {
    }

    //region Properties
    public boolean getWillAccept() {
        return this.willAccept;
    }
    public void setWillAccept(boolean willAccept) {
        this.willAccept = willAccept;
    }
    private Player myreciever;
    //endregion

    //region Overrides
    @Override
    public void action(DataModel model) {
        myreciever = model.getPlayers()[model.getTradeOffer().getReceiver()];
        if (this.willAccept) {
            TradeOffer t = model.getTradeOffer();
            Player[] players = model.getPlayers();
            Player sender = players[t.getSender()];
            Player receiver = players[t.getReceiver()];
            myreciever = players[t.getReceiver()];
            sender.getResources().removeRange(t.getOffer());
            receiver.getResources().addRange(t.getOffer());
        }
        model.setTradeOffer(null);
    }
    @Override
    protected MessageLine getLog(DataModel model) {
        if (this.willAccept) {
            return new MessageLine(myreciever.getName(), "The trade was accepted");
        }
        return new MessageLine(myreciever.getName(), "The trade was not accepted");
    }
    //endregion
}
