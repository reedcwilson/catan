package com.catan.main.datamodel.map;

import com.catan.main.datamodel.hexgrid.hex.HexLocation;

public class WaterHex extends CatanHex {

    public WaterHex(HexLocation hl) {
        super(hl);
    }

    @Override
    public boolean equals(Object o) {
        if (super.equals(o)) {
            return o instanceof WaterHex;
        }
        return true;
    }
}
