package com.catan.main.server;

import com.catan.main.datamodel.player.Color;
import com.catan.main.datamodel.player.Player;

public class GsonPlayer {
    public Color color;
    public String name;
    public Long id;

    public GsonPlayer()
    {
        this.name = "";
        this.id = -1L;
        this.color = Color.yellow;
    }

    public GsonPlayer(Player player)
    {
        if (player != null)
        {
            this.name = player.getName();
            this.id = player.getPlayerID();
            this.color = player.getColor();
        }
    }
}
