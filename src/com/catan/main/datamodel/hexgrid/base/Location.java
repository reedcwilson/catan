package com.catan.main.datamodel.hexgrid.base;

import com.catan.main.datamodel.hexgrid.edge.EdgeLocation;
import com.catan.main.datamodel.hexgrid.hex.HexLocation;
import com.catan.main.datamodel.hexgrid.vertex.VertexLocation;

public abstract class Location<T extends Direction<T>, L extends Location<T, L>> extends HexLocation {
    protected T direction;

    public Location() {
    }

    public Location(int x, int y, T direction) {
        super(x, y);
        this.direction = direction;
    }

    public Location(HexLocation hd, T direction) {
        this(hd.x, hd.y, direction);
    }

    public T getDirection() {
        return this.direction;
    }

    public abstract EdgeLocation[] getConnectedEdges();

    public abstract VertexLocation[] getNeighborVertices();

    public abstract L[] getEquivalenceGroup();

    public EdgeLocation getEdge(int offset) {
        return new EdgeLocation(this.x, this.y, this.direction.getEdge(offset));
    }

    public VertexLocation getVertex(int offset) {
        return new VertexLocation(this.x, this.y, this.direction.getVertex(offset));
    }

    public String toString() {
        return "direction=" + this.direction + ", x=" + this.x + ", y=" + this.y;
    }

    public int hashCode() {
        int xTotal = 0;
        int yTotal = 0;
        for (L ml : getEquivalenceGroup()) {
            xTotal += ml.x;
            yTotal += ml.y;
        }
        return xTotal * 7 + yTotal * 7;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Location)) {
            return false;
        }
        Location other = (Location) o;
        for (Location vl : other.getEquivalenceGroup()) {
            if (_equals(vl)) {
                return true;
            }
        }
        return false;
    }

    protected boolean _equals(Location vl) {
        if (vl == null) {
            return false;
        }
        return (vl.direction == this.direction) && (vl.x == this.x) && (vl.y == this.y);
    }
}
