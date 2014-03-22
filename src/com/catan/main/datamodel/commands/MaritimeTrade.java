package com.catan.main.datamodel.commands;

import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.map.Resource;
import com.catan.main.datamodel.message.MessageLine;
import com.catan.main.datamodel.player.Bank;
import com.catan.main.datamodel.player.Player;
import com.catan.main.datamodel.player.ResourceHand;

public class MaritimeTrade extends Command {

    //region Fields
    private Resource inputResource;
    private Resource outputResource;
    private int ratio;
    //endregion

    public MaritimeTrade() {
    }

    //region Properties
    public int getRatio() {
        return ratio;
    }
    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    public Resource getInputResource() {
        return this.inputResource;
    }
    public void setInputResource(Resource inputResource) {
        this.inputResource = inputResource;
    }

    public Resource getOutputResource() {
        return this.outputResource;
    }
    public void setOutputResource(Resource outputResource) {
        this.outputResource = outputResource;
    }
    //endregion

    //region Overrides
    @Override
    protected MessageLine getLog(DataModel model) {
        // TODO: what should this return
        return null;
    }
    @Override
    public void action(DataModel model) {
        Player sender = model.getPlayers()[this.getPlayerIndex()];
        Bank bank = model.getBank();
        ResourceHand offer = new ResourceHand(0, 0, 0, 0, 0);
        offer.add(this.inputResource, -this.ratio);
        offer.add(this.outputResource, 1);
        sender.getResources().addRange(offer);
        bank.removeRange(offer);
    }

    @Override
    public String toString() {
        return "MaritimeTrade{" +
                "inputResource=" + inputResource +
                ", outputResource=" + outputResource +
                ", ratio=" + ratio +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MaritimeTrade)) return false;

        MaritimeTrade that = (MaritimeTrade) o;

        if (ratio != that.ratio) return false;
        if (inputResource != that.inputResource) return false;
        if (outputResource != that.outputResource) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = inputResource != null ? inputResource.hashCode() : 0;
        result = 31 * result + (outputResource != null ? outputResource.hashCode() : 0);
        result = 31 * result + ratio;
        return result;
    }
    //endregion
}
