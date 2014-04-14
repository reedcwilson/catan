package com.catan.main.datamodel.commands;


import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.devcard.DevCardType;
import com.catan.main.datamodel.map.Resource;
import com.catan.main.datamodel.message.MessageLine;
import com.catan.main.datamodel.player.Player;

public class BuyDevCard extends Command {

    private int rand = -1;

    public BuyDevCard() {
    }

    //region Overrides
    @Override
    protected MessageLine getLog(DataModel model) {
        this.setPlayerIndex(model.getTurnTracker().getCurrentTurn());
        String name = model.getPlayerName(this.getPlayerIndex());
        return new MessageLine(name, name + " bought a Development Card.");
    }
    @Override
    public void action(DataModel model) {
        if(rand == -1)
            rand = (int) Math.floor(Math.random() * model.getDeck().total());
        DevCardType boughtCard = model.getDeck().removeItem(rand);
        Player player = model.getPlayers()[this.getPlayerIndex()];
        if (boughtCard == DevCardType.Monument) {
            player.getOldDevCards().add(boughtCard, 1);
        } else {
            player.getNewDevCards().add(boughtCard, 1);
        }
        player.getResources().add(Resource.Wheat, -1);
        player.getResources().add(Resource.Sheep, -1);
        player.getResources().add(Resource.Ore, -1);
    }
    //endregion
}
