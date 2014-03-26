package com.catan.main.datamodel.map;

import com.catan.main.datamodel.hexgrid.hex.HexLocation;

public class NormalHex extends CatanHex {

    protected Resource landtype;

    public NormalHex(HexLocation location, Resource resource) {
        super(location);
        this.isLand = true;
        this.landtype = resource;
    }

    public Resource getLandType() {
        return this.landtype;
    }

    //region Overrides
    @Override
    public String toString() {
        return "NormalHex{" +
                "landType=" + landtype +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NormalHex)) return false;
        if (!super.equals(o)) return false;

        NormalHex normalHex = (NormalHex) o;

        if (landtype != normalHex.landtype) return false;

        return true;
    }
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (landtype != null ? landtype.hashCode() : 0);
        return result;
    }
    //endregion
}