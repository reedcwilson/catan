package com.catan.main.datamodel;

import com.catan.main.datamodel.devcard.DevCardDeck;
import com.catan.main.datamodel.map.Map;
import com.catan.main.datamodel.message.MessageBox;
import com.catan.main.datamodel.player.*;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import java.io.Serializable;
import java.util.Arrays;

public class DataModel implements Cloneable, Serializable {

    private static final int _startingArmy = 2;

    //region Fields
    private DevCardDeck deck;
    private Map map;
    private Player[] players;
    private MessageBox log;
    private MessageBox chat;
    private Bank bank;
    private TurnTrackerInterface turnTracker;
    private TradeOffer tradeOffer;
    private int biggestArmy;
    private int longestRoad = -1;
    private Long winner;
    private int revision = 0;
    //endregion

    public DataModel() {
        System.out.println("This constructor is only for compiling");
    }

    @Inject
    public DataModel(TurnTrackerInterface turn) {
        //this.log = new MessageBox();
        //this.chat = new MessageBox();
        //this.bank = new Bank();
        //this.map = map;
        //this.players = players;
        this.turnTracker = turn;
        //this.deck = new DevCardDeck();
        //this.biggestArmy = 2;
        //this.winner = Long.valueOf(-1L);
    }

    //region Invariants
    public Player[] getPlayers() {
        return this.players;
    }
    public void setPlayers(Player[] players){
        this.players = players;
    }
    public int getRevision() {
        return this.revision;
    }
    //endregion

    //region Properties
    public int getLongestRoad() {
        return this.longestRoad;
    }
    public void setLongestRoad(int longestRoad) {
        if (this.longestRoad != -1) {
            this.players[this.longestRoad].setLongestRoad(false);
        }
        if (longestRoad != -1) {
            this.players[longestRoad].setLongestRoad(true);
        }
        this.longestRoad = longestRoad;
    }

    public int getBiggestArmy() {
        return this.biggestArmy;
    }
    public void setBiggestArmy(int biggestArmy) {
        this.biggestArmy = biggestArmy;
    }

    public DevCardDeck getDeck() {
        return this.deck;
    }
    public void setDeck(DevCardDeck deck) {
        this.deck = deck;
    }

    public TradeOffer getTradeOffer() {
        if (this.tradeOffer == null) {
            return new TradeOffer();
        }
        return this.tradeOffer;
    }
    public void setTradeOffer(TradeOffer tradeOffer) {
        this.tradeOffer = tradeOffer;
    }

    public Map getMap() {
        return this.map;
    }
    public void setMap(Map generateNewMap) {
        this.map = generateNewMap;
    }

    public MessageBox getLog() {
        return this.log;
    }
    public void setLog(MessageBox log) {
        this.log = log;
    }

    public MessageBox getChat() {
        return this.chat;
    }
    public void setChat(MessageBox chat) {
        this.chat = chat;
    }

    public Bank getBank() {
        return this.bank;
    }
    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Long getWinner() {
        return this.winner;
    }
    public void setWinner(Long winner) {
        this.winner = winner;
    }

    @Inject
    public TurnTrackerInterface getTurnTracker() {
        return this.turnTracker;
    }


    //endregion

    //region Methods
    public String getPlayerName(int orderIndex) {
        return this.players[orderIndex].getName();
    }

    public int currentTurn() {
        return this.turnTracker.getCurrentTurn();
    }

    public void advanceVersion() {
        this.revision += 1;
    }

    public String toJSON() {
        return new Gson().toJson(this);
    }
    //endregion

    //region Overrides
    @Override
    public DataModel clone() {
        XStream xm = new XStream(new StaxDriver());
        return (DataModel) xm.fromXML(xm.toXML(this));
    }

    @Override
    public String toString() {
        return "DataModel{" +
                "deck=" + deck +
                ", map=" + map +
                ", players=" + Arrays.toString(players) +
                ", log=" + log +
                ", chat=" + chat +
                ", bank=" + bank +
                ", turnTracker=" + turnTracker +
                ", tradeOffer=" + tradeOffer +
                ", biggestArmy=" + biggestArmy +
                ", longestRoad=" + longestRoad +
                ", winner=" + winner +
                ", revision=" + revision +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataModel)) return false;

        DataModel that = (DataModel) o;

        if (biggestArmy != that.biggestArmy) return false;
        if (longestRoad != that.longestRoad) return false;
        if (revision != that.revision) return false;
        if (bank != null ? !bank.equals(that.bank) : that.bank != null) return false;
        if (chat != null ? !chat.equals(that.chat) : that.chat != null) return false;
        if (deck != null ? !deck.equals(that.deck) : that.deck != null) return false;
        if (log != null ? !log.equals(that.log) : that.log != null) return false;
        if (map != null ? !map.equals(that.map) : that.map != null) return false;
        if (!Arrays.equals(players, that.players)) return false;
        if (tradeOffer != null ? !tradeOffer.equals(that.tradeOffer) : that.tradeOffer != null) return false;
        if (turnTracker != null ? !turnTracker.equals(that.turnTracker) : that.turnTracker != null) return false;
        if (winner != null ? !winner.equals(that.winner) : that.winner != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = deck != null ? deck.hashCode() : 0;
        result = 31 * result + (map != null ? map.hashCode() : 0);
        result = 31 * result + (players != null ? Arrays.hashCode(players) : 0);
        result = 31 * result + (log != null ? log.hashCode() : 0);
        result = 31 * result + (chat != null ? chat.hashCode() : 0);
        result = 31 * result + (bank != null ? bank.hashCode() : 0);
        result = 31 * result + (turnTracker != null ? turnTracker.hashCode() : 0);
        result = 31 * result + (tradeOffer != null ? tradeOffer.hashCode() : 0);
        result = 31 * result + biggestArmy;
        result = 31 * result + longestRoad;
        result = 31 * result + (winner != null ? winner.hashCode() : 0);
        result = 31 * result + revision;
        return result;
    }
    //endregion

}
