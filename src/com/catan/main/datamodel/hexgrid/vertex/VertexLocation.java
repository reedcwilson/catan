package com.catan.main.datamodel.hexgrid.vertex;

import com.catan.main.datamodel.hexgrid.base.Location;
import com.catan.main.datamodel.hexgrid.edge.EdgeDirection;
import com.catan.main.datamodel.hexgrid.edge.EdgeLocation;
import com.catan.main.datamodel.hexgrid.hex.HexLocation;

public class VertexLocation extends Location<VertexDirection, VertexLocation> {

    public VertexLocation() {
    }
    public VertexLocation(int x, int y, VertexDirection direction) {
        super(x, y, direction);
    }
    public VertexLocation(HexLocation hd, VertexDirection direction) {
        super(hd, direction);
    }

    //region Public Interface
    public EdgeLocation[] getConnectedEdges() {
        EdgeLocation[] returnValues =
                {
                        new EdgeLocation(this, this.direction.getEdge(-1)),
                        new EdgeLocation(this, this.direction.getEdge(1)),
                        new EdgeLocation(getLocation(getEdge(-1).getDirection()), this.direction.getEdge(2))
                };
        return returnValues;
    }
    public VertexLocation[] getNeighborVertices() {
        VertexLocation[] equivalenceGroup = getEquivalenceGroup();
        VertexLocation[] returnValues =
                {
                        equivalenceGroup[0].getVertex(1),
                        equivalenceGroup[1].getVertex(1),
                        equivalenceGroup[2].getVertex(1)
                };


        return returnValues;
    }
    public VertexLocation[] getEquivalenceGroup() {
        VertexLocation[] returnValues =
                {
                        this,
                        new VertexLocation(getLocation((this.direction).getEdge(-1)),getDirection().rotate(2)),
                        new VertexLocation(getLocation(this.direction.getEdge(1)),(getDirection()).rotate(-2))
                };
        return returnValues;
    }
    //endregion

    //region Overrides
    @Override
    public VertexLocation clone() {
        return new VertexLocation(this.x, this.y, (VertexDirection) this.direction);
    }
    @Override
    public String toString() {
        return "VertexLocation [direction=" + this.direction + ", x=" + this.x + ", y=" + this.y + "]";
    }
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof VertexLocation)) {
            return false;
        }
        VertexLocation other = (VertexLocation) o;
        for (VertexLocation vl : other.getEquivalenceGroup()) {
            if (vl != null && (vl.direction == this.direction) && (vl.x == this.x) && (vl.y == this.y)) {
                return true;
            }
        }
        return false;
    }
    //endregion
}
