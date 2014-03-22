package com.catan.main.datamodel.commands;

import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.message.MessageLine;
import com.catan.main.datamodel.player.ResourceHand;
import com.catan.main.datamodel.player.TradeOffer;

public class OfferTrade extends Command {

    //region Fields
    private int receiver;
    private ResourceHand offer;
    //endregion

    public OfferTrade() {
    }

    //region Properties
    public int getSender() {
        return this.getPlayerIndex();
    }
    public void setSender(int sender) {
        this.setPlayerIndex(sender);
    }

    public int getReceiver() {
        return this.receiver;
    }
    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public ResourceHand getOffer() {
        return this.offer;
    }
    public void setOffer(ResourceHand offer) {
        this.offer = offer;
    }
    //endregion

    //region Overrides
    @Override
    public void action(DataModel model) {
        model.setTradeOffer(new TradeOffer(this.offer, this.getPlayerIndex(), this.receiver));
        System.out.println("just set trade offer.");
        TradeOffer offer = model.getTradeOffer();
        if (offer != null) {
            if (offer.getOffer() != null) {
                System.out.println(offer.getOffer().toString());
            } else {
                System.out.println("empty offer");
            }
        } else {
            System.out.println("tradeoffer was null");
        }
    }

    @Override
    protected MessageLine getLog(DataModel model) {
        return null;
    }

    @Override
    public String toString() {
        return "OfferTrade{" +
                "receiver=" + receiver +
                ", offer=" + offer +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OfferTrade)) return false;

        OfferTrade that = (OfferTrade) o;

        if (receiver != that.receiver) return false;
        if (offer != null ? !offer.equals(that.offer) : that.offer != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = receiver;
        result = 31 * result + (offer != null ? offer.hashCode() : 0);
        return result;
    }
    //endregion
}
