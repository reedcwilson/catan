package com.catan.main.server;

import com.catan.main.datamodel.game.Game;

public class GsonGame {

    public String title;
    public Long id;
    public GsonPlayer[] players = { null, null, null, null };

    public GsonGame(Game g)
    {
        this.title = g.getTitle();
        this.id = g.getId();
        for (int i = 0; i < 4; i++) {
            this.players[i] = new GsonPlayer(g.getModel().getPlayers()[i]);
        }
    }
}
