package com.catan.main.datamodel.hexgrid.vertex;

import com.catan.main.datamodel.hexgrid.base.Value;

public class VertexValue extends Value {

    //region Fields
    protected int worth;
    //endregion

    public VertexValue(int owner, int worth) {
        super(owner);
        this.worth = worth;
    }

    //region Properties
    public int getWorth() {
        return worth;
    }
    public void setWorth(int worth) {
        this.worth = worth;
    }
    //endregion

    //region Overrides
    @Override
    public String toString() {
        return "VertexValue{" +
                "worth=" + worth +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VertexValue)) return false;
        if (!super.equals(o)) return false;

        VertexValue that = (VertexValue) o;

        if (worth != that.worth) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + worth;
        return result;
    }
    //endregion
}
