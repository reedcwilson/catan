package com.catan.main.datamodel.player;

import com.catan.main.datamodel.map.Resource;

import java.io.Serializable;

public class Bank extends ResourceHand implements Serializable {

    public Bank() {
        for (Resource resource : Resource.values()) {
            add(resource, 24);
        }
    }
    public Bank(ResourceHand resourceHand) {
        addRange(resourceHand);
    }

    //region Methods
    public void giveResourcesToPlayers(ResourceHand[] resourcesFromRoll, Player[] players) {
        assert (resourcesFromRoll.length == players.length);
        for (int count = 0; count < players.length; count++) {
            giveResourcesToPlayer(players[count], resourcesFromRoll[count]);
        }
    }
    public void giveResourcesToPlayer(Player player, ResourceHand hand) {
        reconcileHand(hand);
        player.getResources().addRange(hand);
        removeRange(hand);
    }
    private void reconcileHand(ResourceHand rh) {
        Resource[] resources = super.getValues();
        for (Resource r : resources) {
            if (get(r) < rh.get(r)) {
                rh.add(r, get(r) - rh.get(r));
            }
        }
    }
    //endregion
}
