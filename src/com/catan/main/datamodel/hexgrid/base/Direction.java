package com.catan.main.datamodel.hexgrid.base;

import com.catan.main.datamodel.hexgrid.edge.EdgeDirection;
import com.catan.main.datamodel.hexgrid.vertex.VertexDirection;

public abstract interface Direction<T> {

    public abstract T rotate(int paramInt);

    public abstract EdgeDirection getEdge(int paramInt);

    public abstract VertexDirection getVertex(int paramInt);
}
