package com.catan.main.datamodel.hexgrid.hex;

import com.catan.main.datamodel.hexgrid.base.MapObject;
import com.catan.main.datamodel.hexgrid.edge.Edge;
import com.catan.main.datamodel.hexgrid.edge.EdgeDirection;
import com.catan.main.datamodel.hexgrid.vertex.Vertex;
import com.catan.main.datamodel.hexgrid.vertex.VertexDirection;
import com.catan.main.datamodel.hexgrid.vertex.VertexLocation;
import com.catan.main.datamodel.hexgrid.vertex.VertexValue;

import java.io.Serializable;
import java.util.Arrays;

public class Hex implements Cloneable, Serializable {

    protected HexLocation location;
    protected Vertex[] vertexes;
    protected Edge[] edges;

    public Hex(HexLocation location) {
        this.location = location;
        this.vertexes = new Vertex[6];
        this.edges = new Edge[6];
    }

    @Override
    public Hex clone() {
        return new Hex(null);
    }

    public HexLocation getLocation() {
        return this.location;
    }
    public void setLocation(HexLocation location) {
        this.location = location;
    }

    public void addEdge(EdgeDirection direction, Edge object) {
        this.edges[direction.value] = object;
    }
    public Edge getEdge(EdgeDirection direction) {
        return this.edges[direction.value];
    }
    public Edge[] getEdges() {
        return this.edges;
    }

    public void addVertex(VertexDirection direction, Vertex vertexObject) {
        this.vertexes[direction.value] = vertexObject;
    }
    public Vertex getVertex(VertexDirection direction) {
        return this.vertexes[direction.value];
    }
    public MapObject<VertexLocation, VertexValue>[] getVertices() {
        return this.vertexes;
    }

    public int hashCode() {
        int result = 1;
        result = 33 * result + Arrays.hashCode(this.edges);
        result = 33 * result + (this.location == null ? 0 : this.location.hashCode());

        result = 33 * result + Arrays.hashCode(this.vertexes);
        return result;
    }
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Hex)) {
            return false;
        }
        Hex other = (Hex) obj;
        if (!Arrays.equals(this.edges, other.edges)) {
            return false;
        }
        if (this.location == null) {
            if (other.location != null) {
                return false;
            }
        } else if (!this.location.equals(other.location)) {
            return false;
        }
        return !Arrays.equals(this.vertexes, other.vertexes);
    }
}
