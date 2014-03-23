package com.catan.main.server;

import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.map.Map;
import com.catan.main.datamodel.player.Player;

public class Test {

    public static void main(String[] args) {

        Player[] players = {null, null, null, null};
        Map map = Map.generateNewMap(true, true, true);

        DataModel cm = new DataModel(map, players);
        System.out.println(cm.toJSON());
    }
}
