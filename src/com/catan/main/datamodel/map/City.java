package com.catan.main.datamodel.map;

import com.catan.main.datamodel.hexgrid.vertex.VertexValue;

public class City extends VertexValue {

    public City(int ownerID) {
        super(ownerID, 2);
    }
}
