package com.catan.main.datamodel.game;

import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.CommandHistory;
import com.catan.main.datamodel.map.Map;
import com.catan.main.datamodel.player.Color;
import com.catan.main.datamodel.player.Player;

public class Game {

    private static final int _players = 4;

    //region Fields
    private Long id;
    private DataModel model;
    private DataModel start;
    private CommandHistory history;
    //endregion

    public Game(Long id, DataModel model, CommandHistory configuredHistory) {
        this.id = id;
        setModel(model);
        setDefaultModel(model);
        this.history = configuredHistory;
        this.history.executeAll(model);
    }

    public static Game requestNewGame(CreateGameRequest request) {
        Player[] players = {null, null, null, null};
        Map map = Map.generateNewMap(request.randomTiles, request.randomNumbers, request.randomNumbers);

        DataModel model = new DataModel(map, players);
        Game game = new Game(null, model, new CommandHistory(true, false));

        return game;
    }

    //region Properties

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public DataModel getModel() {
        return this.model;
    }
    public void setModel(DataModel model) {
        this.model = model;
    }
    public void setDefaultModel(DataModel model) {
        this.start = model.clone();
    }

    public boolean addPlayer(Long playerId, Color color, String playerName) {
        int turnIndex = getTurnIndex(playerId);
        return turnIndex == -1 ? false : addPlayer(playerId, color, playerName, turnIndex);
    }
    private boolean addPlayer(Long playerId, Color color, String playerName, int turnIndex) {
        Player p = this.model.getPlayers()[turnIndex];
        if ((p == null) || (p.getPlayerID().longValue() == -1L)) {
            return addPlayer(new Player(playerId, turnIndex, color, playerName), turnIndex);
        }
        if (p.getPlayerID() == playerId) {
            return updatePlayer(this.model.getPlayers()[turnIndex], color);
        }
        return false;
    }
    //endregion

    //region Public Interface
    public CommandHistory getHistory() {
        return this.history;
    }

    public void reset() {
        this.model = this.start.clone();
        this.history.getCommands().clear();
    }
    //endregion

    //region Helper Methods
    private int getTurnIndex(Long targetIndex) {
        for (int i = 0; i < _players; i++) {
            if ((this.model.getPlayers()[i] == null) || (this.model.getPlayers()[i].getPlayerID() == targetIndex)) {
                return i;
            }
        }
        return -1;
    }

    private boolean updatePlayer(Player p, Color c) {
        p.update(c);
        return true;
    }
    private boolean addPlayer(Player player, int turnIndex) {
        this.model.getPlayers()[turnIndex] = player;
        return true;
    }
    //endregion
}
