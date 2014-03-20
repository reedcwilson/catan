package com.catan.main.datamodel.commands;


import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.devcard.DevCardType;
import com.catan.main.datamodel.map.Resource;
import com.catan.main.datamodel.message.MessageLine;
import com.catan.main.datamodel.player.Player;

public class BuyDevCard extends Command {

    public BuyDevCard() {
    }

    //region Overrides
    @Override
    protected MessageLine getLogMessage(DataModel model) {
        this.setPlayerIndex(model.getTurnTracker().getCurrentTurn());
        String name = model.getPlayerName(this.getPlayerIndex());
        return new MessageLine(name, name + " bought a Development Card.");
    }
    @Override
    public void doExecute(DataModel model) {
        DevCardType boughtCard = (DevCardType) model.getDeck().removeRandomItem();
        Player ap = model.getPlayers()[this.getPlayerIndex()];
        if (boughtCard == DevCardType.Monument) {
            ap.getOldDevCards().add(boughtCard, 1);
        } else {
            ap.getNewDevCards().add(boughtCard, 1);
        }
        ap.getResources().add(Resource.Wheat, -1);
        ap.getResources().add(Resource.Sheep, -1);
        ap.getResources().add(Resource.Ore, -1);
    }
    //endregion
}
