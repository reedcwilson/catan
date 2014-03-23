package com.catan.main.datamodel.commands;

import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.devcard.DevCardType;
import com.catan.main.datamodel.message.MessageLine;
import com.catan.main.datamodel.player.Player;

public class Monument extends DevCard {

    public Monument() {
        super(DevCardType.Monument);
    }

    //region Overrides
    @Override
    protected void playDevCard(DataModel model) {
        Player player = model.getPlayers()[model.currentTurn()];
        if (model.getWinner().longValue() == -1L) {
            player.addVictoryPoint();
            if (player.wonGame()) {
                model.setWinner(player.getPlayerID());
            }
        }
    }

    @Override
    protected MessageLine getLog(DataModel model) {
        String name = model.getPlayerName(model.currentTurn());
        return new MessageLine(name, name + " built a monument and gained a victory point");
    }
    //endregion
}
