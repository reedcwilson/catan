package com.catan.main.server;

import com.catan.main.datamodel.User;

public class Client {

    //region Fields
    private String timestamp;
    private Long playerID;
    private Long gameID;
    //endregion

    //region Constructors
    public Client(String timestamp, long playerID, long gameID) {
        this.playerID = playerID;
        this.gameID = gameID;
        this.timestamp = timestamp;
    }
    public Client(User user) {
        this.playerID = user.getId();
        this.gameID = null;
        this.timestamp = "";
    }
    //endregion

    //region Properties
    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public long getPlayerID() {
        return this.playerID;
    }

    public void setPlayerID(long playerID) {
        this.playerID = playerID;
    }

    public long getGameID() {
        return this.gameID;
    }

    public void setGameID(long gameID) {
        this.gameID = gameID;
    }
    //endregion

    //region Overrides
    @Override
    public String toString() {
        return "Client{" +
                "timestamp='" + timestamp + '\'' +
                ", playerID=" + playerID +
                ", gameID=" + gameID +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;

        Client client = (Client) o;

        if (gameID != null ? !gameID.equals(client.gameID) : client.gameID != null) return false;
        if (playerID != null ? !playerID.equals(client.playerID) : client.playerID != null) return false;
        if (timestamp != null ? !timestamp.equals(client.timestamp) : client.timestamp != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = timestamp != null ? timestamp.hashCode() : 0;
        result = 31 * result + (playerID != null ? playerID.hashCode() : 0);
        result = 31 * result + (gameID != null ? gameID.hashCode() : 0);
        return result;
    }
    //endregion
}
