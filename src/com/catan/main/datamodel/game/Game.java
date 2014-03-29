package com.catan.main.datamodel.game;

import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.CommandHistory;
import com.catan.main.datamodel.devcard.DevCardDeck;
import com.catan.main.datamodel.map.Map;
import com.catan.main.datamodel.message.MessageBox;
import com.catan.main.datamodel.player.*;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Game {

    private static final int _players = 4;

    //region Fields
    private Long id;
    private String title;
    private DataModel model;
    private DataModel start;
    private CommandHistory history;
    //endregion

    public Game(Long id, String title, DataModel model, CommandHistory configuredHistory) {
        this.id = id;
        this.title = title;
        setModel(model);
        setDefaultModel(model);
        this.history = configuredHistory;
        this.history.executeAll(model);
    }

    public static Game requestNewGame(CreateGameRequest request) {
        Player[] players = {null, null, null, null};
        Map map = Map.generateNewMap(request.randomTiles, request.randomNumbers, request.randomNumbers);
        Injector injector = Guice.createInjector(new TurnTrackerModule());
        //DataModel model = new DataModel(map, players);
        DataModel model =  injector.getInstance(DataModel.class);
        model.setBank(new Bank());
        model.setLog(new MessageBox());
        model.setChat(new MessageBox());
        model.setMap(map);
        model.setPlayers(players);
        model.setDeck(new DevCardDeck());
        model.setBiggestArmy(2);
        model.setWinner(Long.valueOf(-1L));
        Game game = new Game(null, request.getName(), model, new CommandHistory(true, false));

        return game;
    }

    //region Properties

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
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
