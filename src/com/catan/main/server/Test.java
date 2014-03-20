package com.catan.main.server;

import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.map.Map;
import com.catan.main.datamodel.player.Player;
import com.google.gson.Gson;

import java.io.File;
import java.util.Scanner;

public class Test {
    static Gson gson = new Gson();

    public static void main(String[] args) {
        String json = null;
        try {
            json = new Scanner(new File("./src/com/catan/main/test.json")).useDelimiter("\\Z").next();
        } catch (Exception ex) {
            System.out.println("didn't work");
        }

        Player[] players = {null, null, null, null};
        Map map = Map.generateNewMap(true, true, true);

        DataModel cm = new DataModel(map, players);
        System.out.println(cm.toJSON());


        DataModel model = gson.fromJson(json, DataModel.class);
        System.out.println(gson.toJson(model));
    }
}
