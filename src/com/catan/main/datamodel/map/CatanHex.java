package com.catan.main.datamodel.map;

import com.catan.main.datamodel.hexgrid.hex.Hex;
import com.catan.main.datamodel.hexgrid.hex.HexLocation;

import java.io.Serializable;
import java.util.Arrays;

public class CatanHex extends Hex {

    protected boolean isLand;

    public CatanHex(HexLocation location) {
        super(location);
    }

    public boolean getIsLand() {
        return this.isLand;
    }

    //region Overrides
    @Override
    public String toString() {
        return "CatanHex{" +
                "isLand=" + isLand +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CatanHex)) return false;
        if (!super.equals(o)) return false;

        CatanHex catanHex = (CatanHex) o;

        if (isLand != catanHex.isLand) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (isLand ? 1 : 0);
        return result;
    }
    //endregion
}
