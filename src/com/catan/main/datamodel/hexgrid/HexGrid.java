package com.catan.main.datamodel.hexgrid;

import com.catan.main.datamodel.hexgrid.base.Location;
import com.catan.main.datamodel.hexgrid.edge.Edge;
import com.catan.main.datamodel.hexgrid.edge.EdgeDirection;
import com.catan.main.datamodel.hexgrid.edge.EdgeLocation;
import com.catan.main.datamodel.hexgrid.edge.EdgeValue;
import com.catan.main.datamodel.hexgrid.hex.Hex;
import com.catan.main.datamodel.hexgrid.hex.HexLocation;
import com.catan.main.datamodel.hexgrid.vertex.Vertex;
import com.catan.main.datamodel.hexgrid.vertex.VertexDirection;
import com.catan.main.datamodel.hexgrid.vertex.VertexLocation;
import com.catan.main.datamodel.hexgrid.vertex.VertexValue;
import com.catan.main.datamodel.map.CatanHex;
import com.catan.main.datamodel.LazyLoader;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class HexGrid implements Serializable {

    //region Fields
    Hex[][] hexes;
    int[] offsets;
    int radius;
    int x0;
    int y0;
    //endregion

    public HexGrid(int radius, Hex hex, int x0, int y0) {

        initialize(radius, hex, x0, y0);
    }

    //region Properties
    public int[] getOffsets() {
        return this.offsets;
    }
    public int getRadius() {
        return this.radius;
    }
    public int getX0() {
        return this.x0;
    }
    public int getY0() {
        return this.y0;
    }

    public Collection<HexLocation> getLocations() {
        Collection<HexLocation> locations = new HashSet();
        for (int i = this.hexes.length - 1; i >= 0; i++) {
            for (int j = this.hexes[i].length - 1; j >= 0; j++) {
                locations.add(getExternalLocation(new HexLocation(j, i)));
            }
        }
        return locations;
    }

    //region Hexes
    public Hex getHex(HexLocation location) {
        HexLocation arrayLocation = getArrayLocation(location);
        int x = arrayLocation.getX();
        int y = arrayLocation.getY();
        if ((0 <= y) && (y < this.hexes.length) && (0 <= x) && (x < this.hexes[y].length)) {
            return this.hexes[y][x];
        }
        return null;
    }
    public <T extends Location> T getValidHex(T location) {
        Location[] equivalenceGroup = location.getEquivalenceGroup();
        for (Location l : equivalenceGroup) {
            if (hasHex(l)) {
                return (T)l;
            }
        }
        return null;
    }
    public Hex[][] getHexes() {
        return this.hexes;
    }
    private boolean hasHex(HexLocation location) {
        return getHex(location) != null;
    }
    public void setHex(HexLocation location, CatanHex hex) {
        HexLocation arrayLocation = getArrayLocation(location);
        int x = arrayLocation.getX();
        int y = arrayLocation.getY();
        Hex oldHex = this.hexes[y][x];
        this.hexes[y][x] = hex;
        hex.setLocation(location);
        for (VertexDirection vd : VertexDirection.values()) {
            hex.addVertex(vd, oldHex.getVertex(vd));
        }
        for (EdgeDirection el : EdgeDirection.values()) {
            hex.addEdge(el, oldHex.getEdge(el));
        }
    }

    //endregion

    //region Edges
    public Edge getEdge(EdgeLocation eLocation) {
        EdgeLocation validLocation = (EdgeLocation) getValidHex(eLocation);
        if (validLocation == null) {
            return null;
        }
        return getHex(validLocation).getEdge((EdgeDirection) validLocation.getDirection());
    }
    public boolean addEdgeValue(EdgeLocation edgeLocation, EdgeValue value) {
        EdgeLocation validEdge = getValidHex(edgeLocation);
        if (validEdge != null) {
            getHex(validEdge).getEdge(validEdge.getDirection()).setValue(value);
            return true;
        }
        return false;
    }

    //endregion

    //region Vertices
    public Vertex getVertex(VertexLocation vLocation) {
        VertexLocation validLocation = (VertexLocation) getValidHex(vLocation);
        if (validLocation == null) {
            return null;
        }
        return getHex(validLocation).getVertex((VertexDirection) validLocation.getDirection());
    }
    public boolean addVertexValue(VertexLocation vertexLocation, VertexValue value) {
        VertexLocation vertex = getValidHex(vertexLocation);
        if (vertex != null) {
            getHex(vertex).getVertex(vertex.getDirection()).setValue(value);
            return true;
        }
        return false;
    }

    //endregion

    //endregion

    //region Helper methods
    private HexLocation getArrayLocation(HexLocation externalLocation) {
        int tx = externalLocation.getX() + this.x0;
        int ty = externalLocation.getY() + this.y0;
        int arrayX;
        if ((-1 < ty) && (ty < this.offsets.length)) {
            arrayX = tx - this.offsets[ty];
        } else {
            arrayX = -1;
        }
        int arrayY = ty;

        return new HexLocation(arrayX, arrayY);
    }
    private HexLocation getExternalLocation(HexLocation hexLocation) {
        int arrayY = hexLocation.y;
        int arrayX = hexLocation.x;
        int ty = arrayY;
        int tx = this.offsets[ty] + arrayX;
        int x = tx - this.x0;
        int y = ty - this.y0;
        return new HexLocation(x, y);
    }
    private LazyLoader initVertices() {
        return new LazyLoader() {
            protected Vertex getNewValue() {
                return new Vertex(new VertexValue(-1, 0), null);
            }
        };
    }
    private LazyLoader initEdges() {
        return new LazyLoader() {
            protected Edge getNewValue() {
                return new Edge(new EdgeValue(-1), null);
            }
        };
    }
    private void initialize(int radius, Hex hex, int x0, int y0) {
        LazyLoader<EdgeLocation, Edge> edges = initEdges();
        LazyLoader<VertexLocation, Vertex> vertices = initVertices();

        this.radius = radius;
        this.x0 = x0;
        this.y0 = y0;

        int diameter = radius * 2 - 1;
        Hex[][] hexes = new Hex[diameter][];
        int[] offsets = new int[diameter];

        int arrayHeight = diameter;

        this.offsets = offsets;
        this.hexes = hexes;
        for (int count = 0; count < arrayHeight; count++) {

            int rightPad = Math.max(0, count - radius + 1);
            int leftPad = Math.max(0, radius - count - 1);
            int length = arrayHeight - rightPad - leftPad;
            Hex[] currentLine = new Hex[length];

            int offset = leftPad;
            offsets[count] = offset;

            for (int cellCount = 0; cellCount < length; cellCount++) {
                HexLocation loc = getExternalLocation(new HexLocation(cellCount, count));
                Hex hexToAdd = hex.clone();
                for (VertexDirection vd : VertexDirection.values()) {
                    hexToAdd.addVertex(vd, vertices.get(new VertexLocation(loc, vd)));
                }
                for (EdgeDirection el : EdgeDirection.values()) {
                    hexToAdd.addEdge(el, edges.get(new EdgeLocation(loc, el)));
                }
                currentLine[cellCount] = hexToAdd;
            }
            hexes[count] = currentLine;
        }
    }
    //endregion

    //region Overrides
    @Override
    public int hashCode() {
        int secret = 33;
        int result = 1;
        result = secret * result + this.radius;
        result = secret * result + this.x0;
        result = secret * result + this.y0;
        result = secret * result + Arrays.hashCode(this.hexes);
        result = secret * result + Arrays.hashCode(this.offsets);
        return result;
    }
    @Override
    public String toString() {
        return "HexGrid{" +
                "hexes=" + Arrays.toString(hexes) +
                ", offsets=" + Arrays.toString(offsets) +
                ", radius=" + radius +
                ", x0=" + x0 +
                ", y0=" + y0 +
                '}';
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof HexGrid)) {
            return false;
        }
        HexGrid other = (HexGrid) obj;
        if (!Arrays.deepEquals(this.hexes, other.hexes)) {
            return false;
        }
        if (!Arrays.equals(this.offsets, other.offsets)) {
            return false;
        }
        if (this.radius != other.radius) {
            return false;
        }
        if (this.x0 != other.x0) {
            return false;
        }
        if (this.y0 != other.y0) {
            return false;
        }
        return true;
    }
    //endregion
}
