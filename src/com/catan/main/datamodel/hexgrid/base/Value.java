package com.catan.main.datamodel.hexgrid.base;

public abstract class Value
{
    protected int worth = -1;
    protected int ownerID = -1;

    public Value() {}

    public Value(int ownerID, int worth)
    {
        this.ownerID = ownerID;
        this.worth = worth;
    }

    public int getOwnerID()
    {
        return this.ownerID;
    }
    public int getWorth() { return this.worth; }

    //region Overrides

    @Override
    public String toString() {
        return "Value{" +
                "ownerID=" + ownerID +
                ", value=" + worth +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Value)) return false;

        Value value1 = (Value) o;

        if (ownerID != value1.ownerID) return false;
        if (worth != value1.worth) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ownerID;
        result = 31 * result + worth;
        return result;
    }
//endregion
}
