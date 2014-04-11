package com.catan.main.datamodel.player;

import com.catan.main.datamodel.Hand;
import com.catan.main.datamodel.map.Resource;

import java.io.Serializable;

public class ResourceHand extends Hand<Resource> implements Serializable {

    //region Fields
    private int brick;
    private int wood;
    private int sheep;
    private int wheat;
    private int ore;
    //endregion

    public ResourceHand(int brick, int wood, int sheep, int wheat, int ore) {
        this.brick = brick;
        this.wood = wood;
        this.sheep = sheep;
        this.wheat = wheat;
        this.ore = ore;
    }

    public ResourceHand() {
    }

    //region Properties
    public int getBrick() {
        return brick;
    }
    public void setBrick(int brick) {
        this.brick = brick;
    }

    public int getWood() {
        return wood;
    }
    public void setWood(int wood) {
        this.wood = wood;
    }

    public int getSheep() {
        return sheep;
    }
    public void setSheep(int sheep) {
        this.sheep = sheep;
    }

    public int getWheat() {
        return wheat;
    }
    public void setWheat(int wheat) {
        this.wheat = wheat;
    }

    public int getOre() {
        return ore;
    }
    public void setOre(int ore) {
        this.ore = ore;
    }
    //endregion

    //region Overrides
    @Override
    protected Resource getInstance() {
        return Resource.Brick;
    }

    @Override
    public int get(Resource resource) {
        switch (resource) {
            case Brick:
                return brick;
            case Ore:
                return ore;
            case Wood:
                return wood;
            case Wheat:
                return wheat;
            case Sheep:
                return sheep;
            default:
                return 0;
        }
    }

    @Override
    public void add(Resource resource, int i) {
        switch (resource) {
            case Brick:
                brick += i;
                break;
            case Ore:
                ore += i;
                break;
            case Wood:
                wood += i;
                break;
            case Wheat:
                wheat += i;
                break;
            case Sheep:
                sheep += i;
                break;
            default:
                break;
        }
    }


    @Override
    public int total() {
        return brick + ore + wood + wheat + sheep;
    }

    @Override
    public String toString() {
        return "ResourceHand{" +
                "brick=" + brick +
                ", wood=" + wood +
                ", sheep=" + sheep +
                ", wheat=" + wheat +
                ", ore=" + ore +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResourceHand)) return false;

        ResourceHand that = (ResourceHand) o;

        if (brick != that.brick) return false;
        if (ore != that.ore) return false;
        if (sheep != that.sheep) return false;
        if (wheat != that.wheat) return false;
        if (wood != that.wood) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = brick;
        result = 31 * result + wood;
        result = 31 * result + sheep;
        result = 31 * result + wheat;
        result = 31 * result + ore;
        return result;
    }

    //endregion
}