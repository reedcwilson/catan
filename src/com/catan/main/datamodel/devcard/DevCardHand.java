package com.catan.main.datamodel.devcard;

import com.catan.main.datamodel.Hand;

public class DevCardHand extends Hand<DevCardType> {

    //region Fields
    int yearOfPlenty;
    int monopoly;
    int soldier;
    int roadBuilding;
    int monument;
    //endregion

    public DevCardHand() {
    }

    //region Properties
    public int getYearOfPlenty() {
        return this.yearOfPlenty;
    }
    public void setYearOfPlenty(int yearOfPlenty) {
        this.yearOfPlenty = yearOfPlenty;
    }

    public int getMonopoly() {
        return this.monopoly;
    }
    public void setMonopoly(int monopoly) {
        this.monopoly = monopoly;
    }

    public int getSoldier() {
        return this.soldier;
    }
    public void setSoldier(int soldier) {
        this.soldier = soldier;
    }

    public int getRoadBuilding() {
        return this.roadBuilding;
    }
    public void setRoadBuilding(int roadBuilding) {
        this.roadBuilding = roadBuilding;
    }

    public int getMonument() {
        return this.monument;
    }
    public void setMonument(int monument) {
        this.monument = monument;
    }
    //endregion

    //region Overrides
    @Override
    public int get(DevCardType t) {
        switch (t) {
            case Monument:
                return this.monument;
            case Year_of_Plenty:
                return this.yearOfPlenty;
            case Monopoly:
                return this.monopoly;
            case Road_Building:
                return this.roadBuilding;
            case Soldier:
                return this.soldier;
        }
        throw new RuntimeException("Invalid devcard request");
    }
    @Override
    public void add(DevCardType t, int i) {
        switch (t) {
            case Monument:
                this.monument += i;
                break;
            case Year_of_Plenty:
                this.yearOfPlenty += i;
                break;
            case Monopoly:
                this.monopoly += i;
                break;
            case Road_Building:
                this.roadBuilding += i;
                break;
            case Soldier:
                this.soldier += i;
                break;
            default:
                throw new RuntimeException("Invalid devcard request");
        }
    }
    @Override
    public int total() {
        return this.soldier + this.roadBuilding + this.monopoly + this.yearOfPlenty + this.monument;
    }
    @Override
    protected DevCardType getInstance() {
        return DevCardType.Monopoly;
    }

    @Override
    public String toString() {
        return "DevCardHand{" +
                "yearOfPlenty=" + yearOfPlenty +
                ", monopoly=" + monopoly +
                ", soldier=" + soldier +
                ", roadBuilding=" + roadBuilding +
                ", monument=" + monument +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DevCardHand)) return false;

        DevCardHand that = (DevCardHand) o;

        if (monopoly != that.monopoly) return false;
        if (monument != that.monument) return false;
        if (roadBuilding != that.roadBuilding) return false;
        if (soldier != that.soldier) return false;
        if (yearOfPlenty != that.yearOfPlenty) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = yearOfPlenty;
        result = 31 * result + monopoly;
        result = 31 * result + soldier;
        result = 31 * result + roadBuilding;
        result = 31 * result + monument;
        return result;
    }
    //endregion
}
