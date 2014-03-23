package com.catan.main.datamodel.hexgrid.edge;

import com.catan.main.datamodel.hexgrid.base.MapObject;

public class Edge extends MapObject<EdgeLocation, EdgeValue> {
    public Edge(EdgeValue value, EdgeLocation location) {
        super(value, location);
    }
}
