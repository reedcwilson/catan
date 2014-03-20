package com.catan.main.datamodel.devcard;

public class DevCardDeck extends DevCardHand {

    public DevCardDeck() {
        reset();
    }
    public DevCardDeck(DevCardHand devCardHand) {
        addRange(devCardHand);
    }

    //region Helper Methods
    private DevCardHand getOriginalCounts() {
        DevCardHand originalCounts = new DevCardHand();
        originalCounts.monopoly = 2;
        originalCounts.monument = 5;
        originalCounts.roadBuilding = 2;
        originalCounts.soldier = 14;
        originalCounts.yearOfPlenty = 2;
        return originalCounts;
    }
    //endregion

    //region Public Interface
    public void reset() {
        for (DevCardType type : getValues()) {
            set(type, 0);
        }
        addRange(getOriginalCounts());
    }
    public DevCardType drawCard() {
        return removeRandomItem();
    }
    //endregion
}
