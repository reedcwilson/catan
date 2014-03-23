package com.catan.main.datamodel.map;

import com.catan.main.datamodel.hexgrid.hex.HexLocation;

public class NormalHex extends CatanHex {

    protected Resource landType;

    public NormalHex(HexLocation location, Resource resource) {
        super(location);
        this.isLand = true;
        this.landType = resource;
    }

    public Resource getLandType() {
        return this.landType;
    }

    //region Overrides
    @Override
    public String toString() {
        return "NormalHex{" +
                "landType=" + landType +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NormalHex)) return false;
        if (!super.equals(o)) return false;

        NormalHex normalHex = (NormalHex) o;

        if (landType != normalHex.landType) return false;

        return true;
    }
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (landType != null ? landType.hashCode() : 0);
        return result;
    }
    //endregion
}