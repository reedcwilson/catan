package com.catan.main.datamodel.commands;

import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.devcard.DevCardType;
import com.catan.main.datamodel.message.MessageLine;

public abstract class DevCard extends Command {

    //region Fields
    protected DevCardType cardType;
    //endregion

    protected DevCard(DevCardType cardType) {
        this.cardType = cardType;
    }

    //region Abstract Methods
    protected abstract void playDevCard(DataModel paramDataModel);

    protected abstract MessageLine getLogMessage(DataModel paramDataModel);
    //endregion

    //region Public Interface
    public void doExecute(DataModel model) {
        playDevCard(model);
        model.getPlayers()[this.getPlayerIndex()].getOldDevCards().add(this.cardType, -1);
    }
    //endregion
}
