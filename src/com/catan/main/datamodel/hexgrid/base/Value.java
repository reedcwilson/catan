package com.catan.main.datamodel.hexgrid.base;

import java.io.Serializable;

public abstract class Value implements Serializable
{
    protected int ownerID = -1;

    public Value() {}

    public Value(int ownerID)
    {
        this.ownerID = ownerID;
    }

    public int getOwnerID()
    {
        return this.ownerID;
    }

    //region Overrides

    @Override
    public String toString() {
        return "Value{" +
                "ownerID=" + ownerID +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Value)) return false;

        Value value1 = (Value) o;

        if (ownerID != value1.ownerID) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ownerID * 31;
        return result;
    }
//endregion
}
