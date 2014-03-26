package com.catan.main.datamodel.map;

import com.catan.main.datamodel.hexgrid.hex.HexLocation;

public class DesertHex extends CatanHex {

    public DesertHex(HexLocation location)
    {
        super(location);
        this.isLand = true;
    }

    public boolean equals(java.lang.Object o) {
        return false;
    }
}
