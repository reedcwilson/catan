package com.catan.main.datamodel.hexgrid.vertex;

import com.catan.main.datamodel.hexgrid.base.MapObject;

public class Vertex extends MapObject<VertexLocation, VertexValue> {

    public Vertex(VertexValue value, VertexLocation location) {
        super(value, location);
    }

    public VertexValue getValue() {
        return this.value;
    }
}
