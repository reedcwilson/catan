package com.catan.main.datamodel.player;

import com.catan.main.datamodel.devcard.DevCardHand;
import com.catan.main.datamodel.map.Resource;

import java.io.Serializable;

public class Player implements Serializable {

    private static final int _pointsToWin = 10;

    //region Fields
    private Color color;
    private ResourceHand resources;
    private DevCardHand oldDevCards;
    private DevCardHand newDevCards;
    private int roads;
    private int cities;
    private int settlements;
    private int soldiers;
    private int victoryPoints;
    private int monuments;
    private boolean longestRoad = false;
    private boolean largestArmy = false;
    private boolean playedDevCard = false;
    private boolean discarded = false;
    private Long playerID;
    private int orderNumber;
    private String name;
    //endregion

    public Player(Long playerID, int orderNumber, Color color, String name) {
        assert ((-1 < orderNumber) && (orderNumber < 4));
        this.playerID = playerID;
        this.orderNumber = orderNumber;
        this.name = name;
        this.resources = new ResourceHand();
        this.oldDevCards = new DevCardHand();
        this.newDevCards = new DevCardHand();
        this.roads = 15;
        this.cities = 4;
        this.settlements = 5;
        this.color = color;
    }

    public static int getPointsToWin() {
        return _pointsToWin;
    }

    //region Properties

    //region Invariants
    public Long getPlayerID() {
        return this.playerID;
    }
    public Color getColor() {
        return this.color;
    }
    public String getName() {
        return this.name;
    }
    //endregion

    public boolean isLongestRoad() {
        return this.longestRoad;
    }
    public void setLongestRoad(boolean b) {
        if ((this.longestRoad) && (b)) {
            return;
        }
        if ((!this.longestRoad) && (!b)) {
            return;
        }
        if (b) {
            setVictoryPoints(this.victoryPoints + 2);
        } else {
            setVictoryPoints(this.victoryPoints - 2);
        }
        this.longestRoad = b;
    }

    public ResourceHand getResources() {
        return this.resources;
    }
    public void setResources(ResourceHand resources) {
        this.resources = resources;
    }

    public DevCardHand getOldDevCards() {
        return this.oldDevCards;
    }
    public void setOldDevCards(DevCardHand oldDevCards) {
        this.oldDevCards = oldDevCards;
    }

    public DevCardHand getNewDevCards() {
        return this.newDevCards;
    }
    public void setNewDevCards(DevCardHand newDevCards) {
        this.newDevCards = newDevCards;
    }

    public int getRoads() {
        return this.roads;
    }
    public void setRoads(int roads) {
        this.roads = roads;
    }

    public int getCities() {
        return this.cities;
    }
    public void setCities(int cities) {
        this.cities = cities;
    }

    public int getSettlements() {
        return this.settlements;
    }
    public void setSettlements(int settlements) {
        this.settlements = settlements;
    }

    public int getSoldiers() {
        return this.soldiers;
    }
    public void setSoldiers(int soldiers) {
        this.soldiers = soldiers;
    }

    public int getVictoryPoints() {
        return this.victoryPoints;
    }
    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

    public int getMonuments() {
        return this.monuments;
    }
    public void setMonuments(int monuments) {
        this.monuments = monuments;
    }

    public int getOrderNumber() {
        return this.orderNumber;
    }
    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public boolean hasPlayedDevCard() {
        return this.playedDevCard;
    }
    public void setPlayedDevCard(boolean playedDevCard) {
        this.playedDevCard = playedDevCard;
    }

    public boolean hasDiscarded() {
        return this.discarded;
    }
    public void setDiscarded(boolean discarded) {
        this.discarded = discarded;
    }

    public boolean getLargestArmy() {
        return this.largestArmy;
    }
    public void setLargestArmy(boolean biggest) {
        if ((biggest) && (this.largestArmy)) {
            return;
        }
        if ((!biggest) && (!this.largestArmy)) {
            return;
        }
        if (biggest) {
            setVictoryPoints(this.victoryPoints + 2);
        } else {
            setVictoryPoints(this.victoryPoints - 2);
        }
        this.largestArmy = biggest;
    }
    //endregion

    //region Methods
    public void addVictoryPoint() {
        this.victoryPoints += 1;
    }
    public void subtractVictoryPoint() {
        this.victoryPoints -= 1;
    }

    public boolean wonGame() {
        return this.victoryPoints >= 10;
    }

    public void rob(Player victim, int rand) {
        getResources().add((Resource) victim.resources.removeItem(rand), 1);
    }

    public void update(Color c) {
        this.color = c;
    }
    //endregion

    //region Overrides

    @Override
    public String toString() {
        return "Player{" +
                "color=" + color +
                ", resources=" + resources +
                ", oldDevCards=" + oldDevCards +
                ", newDevCards=" + newDevCards +
                ", roads=" + roads +
                ", cities=" + cities +
                ", settlements=" + settlements +
                ", soldiers=" + soldiers +
                ", victoryPoints=" + victoryPoints +
                ", monuments=" + monuments +
                ", longestRoad=" + longestRoad +
                ", largestArmy=" + largestArmy +
                ", playedDevCard=" + playedDevCard +
                ", discarded=" + discarded +
                ", playerID=" + playerID +
                ", orderNumber=" + orderNumber +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;

        Player player = (Player) o;

        if (cities != player.cities) return false;
        if (discarded != player.discarded) return false;
        if (largestArmy != player.largestArmy) return false;
        if (longestRoad != player.longestRoad) return false;
        if (monuments != player.monuments) return false;
        if (orderNumber != player.orderNumber) return false;
        if (playedDevCard != player.playedDevCard) return false;
        if (roads != player.roads) return false;
        if (settlements != player.settlements) return false;
        if (soldiers != player.soldiers) return false;
        if (victoryPoints != player.victoryPoints) return false;
        if (color != player.color) return false;
        if (name != null ? !name.equals(player.name) : player.name != null) return false;
        if (newDevCards != null ? !newDevCards.equals(player.newDevCards) : player.newDevCards != null) return false;
        if (oldDevCards != null ? !oldDevCards.equals(player.oldDevCards) : player.oldDevCards != null) return false;
        if (playerID != null ? !playerID.equals(player.playerID) : player.playerID != null) return false;
        if (resources != null ? !resources.equals(player.resources) : player.resources != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = color != null ? color.hashCode() : 0;
        result = 31 * result + (resources != null ? resources.hashCode() : 0);
        result = 31 * result + (oldDevCards != null ? oldDevCards.hashCode() : 0);
        result = 31 * result + (newDevCards != null ? newDevCards.hashCode() : 0);
        result = 31 * result + roads;
        result = 31 * result + cities;
        result = 31 * result + settlements;
        result = 31 * result + soldiers;
        result = 31 * result + victoryPoints;
        result = 31 * result + monuments;
        result = 31 * result + (longestRoad ? 1 : 0);
        result = 31 * result + (largestArmy ? 1 : 0);
        result = 31 * result + (playedDevCard ? 1 : 0);
        result = 31 * result + (discarded ? 1 : 0);
        result = 31 * result + (playerID != null ? playerID.hashCode() : 0);
        result = 31 * result + orderNumber;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    //endregion
}
