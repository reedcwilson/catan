package com.catan.main.datamodel.player;

import java.io.Serializable;

public class TradeOffer implements Serializable {

    //region Fields
    private int sender;
    private int receiver;
    private ResourceHand offer;
    //endregion

    public TradeOffer() {
        this.offer = new ResourceHand();
        this.receiver = -1;
        this.sender = -1;
    }
    public TradeOffer(ResourceHand offer, int sender, int receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.offer = offer;
    }

    //region Public Interface
    public ResourceHand getOffer() {
        return this.offer;
    }
    public void setOffer(ResourceHand offer) {
        this.offer = offer;
    }

    public int getSender() {
        return this.sender;
    }
    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getReceiver() {
        return this.receiver;
    }
    public void setReciever(int reciever) {
        this.receiver = reciever;
    }
    //endregion

    //region Overrides

    @Override
    public String toString() {
        return "TradeOffer{" +
                "sender=" + sender +
                ", receiver=" + receiver +
                ", offer=" + offer +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TradeOffer)) return false;

        TradeOffer that = (TradeOffer) o;

        if (receiver != that.receiver) return false;
        if (sender != that.sender) return false;
        if (offer != null ? !offer.equals(that.offer) : that.offer != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sender;
        result = 31 * result + receiver;
        result = 31 * result + (offer != null ? offer.hashCode() : 0);
        return result;
    }

    //endregion

}
