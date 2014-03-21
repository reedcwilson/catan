package com.catan.main.datamodel.commands;

import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.message.MessageLine;

public abstract class Command {

    //region Fields
    private String type;
    private int playerIndex;
    //endregion

    public Command() {
    }

    //region Abstract Methods
    protected abstract MessageLine getLogMessage(DataModel paramDataModel);

    public abstract void doExecute(DataModel paramDataModel);
    //endregion

    //region Properties
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }
    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }
    //endregion

    //region Public Interface
    public void execute(DataModel model) {
        log(model);
        doExecute(model);
    }

    public void log(DataModel model) {
        MessageLine logLine = getLogMessage(model);
        if (logLine != null) {
            model.getLog().addLine(logLine);
        }
    }
    //endregion
}