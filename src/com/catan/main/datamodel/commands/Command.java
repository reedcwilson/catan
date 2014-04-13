package com.catan.main.datamodel.commands;

import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.PersistenceModel;
import com.catan.main.datamodel.message.MessageLine;
import com.catan.main.persistence.DataAccessException;
import com.catan.main.persistence.DataContext;
import com.catan.main.persistence.DataUtils;

import java.io.Serializable;

public abstract class Command implements PersistenceModel, Serializable {

    //region Fields
    private transient DataContext dataContext;
    private Long id;
    private String type;
    private int playerIndex;
    private Long gameId;
    //endregion

    public Command() {
    }

    //region Abstract Methods
    protected abstract MessageLine getLog(DataModel paramDataModel);

    public abstract void action(DataModel paramDataModel) throws DataAccessException;
    //endregion

    //region Properties
    @Override
    public Long getId() {
        return id;
    }
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public byte[] getBytes() {
        return DataUtils.serialize(this);
    }

    public Long getGameId() {
        return gameId;
    }

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
    public void execute(DataModel model) throws DataAccessException {
        action(model);
        log(model);
        if(dataContext != null)
            dataContext.getCommandAccess().insert(this);
    }

    public void log(DataModel model) {
        MessageLine logLine = getLog(model);
        if (logLine != null) {
            model.getLog().addLine(logLine);
        }
    }

    public void initialize(DataContext dataContext, Long gameId) {
        this.dataContext = dataContext;
        this.gameId = gameId;
    }
    //endregion
}