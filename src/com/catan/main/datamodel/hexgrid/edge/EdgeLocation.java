package com.catan.main.datamodel.hexgrid.edge;

import com.catan.main.datamodel.hexgrid.base.Location;
import com.catan.main.datamodel.hexgrid.hex.HexLocation;
import com.catan.main.datamodel.hexgrid.vertex.VertexLocation;

public class EdgeLocation extends Location<EdgeDirection, EdgeLocation> {
    public EdgeLocation() {
    }

    public EdgeLocation(int x, int y, EdgeDirection direction) {
        super(x, y, direction);
    }

    public EdgeLocation(HexLocation hd, EdgeDirection direction) {
        super(hd, direction);
    }

    public EdgeLocation[] getConnectedEdges() {
        EdgeLocation[] equivalenceGroup = getEquivalenceGroup();
        EdgeLocation[] connectedEdges = {equivalenceGroup[0].getEdge(1), equivalenceGroup[1].getEdge(1), equivalenceGroup[1].getEdge(1)};


        return connectedEdges;
    }

    public VertexLocation[] getNeighborVertices() {
        VertexLocation[] returnValues = {getVertex(1), getVertex(-1)};
        return returnValues;
    }

    public EdgeLocation[] getEquivalenceGroup() {
        EdgeLocation[] returnValues = {this, equivalent()};
        return returnValues;
    }

    public EdgeLocation equivalent() {
        return new EdgeLocation(getLocation((EdgeDirection) this.direction), ((EdgeDirection) getDirection()).rotate(3));
    }

    public EdgeLocation clone() {
        return new EdgeLocation(this.x, this.y, (EdgeDirection) this.direction);
    }

    public String toString() {
        return "EdgeLocation [direction=" + this.direction + ", x=" + this.x + ", y=" + this.y + "]";
    }

    public EdgeLocation getCanonical() {
        if ((this.direction == EdgeDirection.N) || (this.direction == EdgeDirection.NW) || (this.direction == EdgeDirection.NE)) {
            return this;
        }
        return equivalent();
    }
}