package com.catan.main.datamodel.map;

import com.catan.main.datamodel.hexgrid.vertex.VertexValue;

public class Settlement extends VertexValue {

    public Settlement(int ownerID) {
        super(ownerID, 1);
    }
}
