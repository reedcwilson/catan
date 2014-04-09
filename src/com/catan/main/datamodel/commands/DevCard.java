package com.catan.main.datamodel.commands;

import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.devcard.DevCardType;
import com.catan.main.datamodel.message.MessageLine;
import com.catan.main.persistence.DataAccessException;

public abstract class DevCard extends Command {

    //region Fields
    protected DevCardType cardType;
    //endregion

    protected DevCard(DevCardType cardType) {
        this.cardType = cardType;
    }

    //region Abstract Methods
    protected abstract void playDevCard(DataModel paramDataModel) throws DataAccessException;

    protected abstract MessageLine getLog(DataModel paramDataModel);
    //endregion

    //region Public Interface
    public void action(DataModel model) throws DataAccessException {
        playDevCard(model);
        model.getPlayers()[this.getPlayerIndex()].getOldDevCards().add(this.cardType, -1);
    }
    //endregion
}
