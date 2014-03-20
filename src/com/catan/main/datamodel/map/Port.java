package com.catan.main.datamodel.map;

import com.catan.main.datamodel.hexgrid.base.MapObject;
import com.catan.main.datamodel.hexgrid.base.Value;
import com.catan.main.datamodel.hexgrid.edge.EdgeDirection;
import com.catan.main.datamodel.hexgrid.edge.EdgeLocation;
import com.catan.main.datamodel.hexgrid.hex.HexLocation;
import com.catan.main.datamodel.hexgrid.vertex.VertexLocation;

public class Port extends MapObject<HexLocation, Value> {

    //region Fields
    private int ratio;
    private Resource inputResource;
    private VertexLocation validVertex1;
    private VertexLocation validVertex2;
    private EdgeDirection orientation;
    //endregion

    public Port(EdgeLocation location, Resource resource) {
        super(null, location);
        setLocation(new HexLocation(location.getX(), location.getY()));
        this.ratio = (resource != null ? 2 : 3);
        this.inputResource = resource;
        this.validVertex1 = location.getVertex(-1);
        this.validVertex2 = location.getVertex(1);
        this.orientation = ((EdgeDirection) location.getDirection());
    }

    //region Properties
    public VertexLocation getValidVertex1() {
        return this.validVertex1;
    }
    public void setValidVertex1(VertexLocation validVertex1) {
        this.validVertex1 = validVertex1;
    }

    public VertexLocation getValidVertex2() {
        return this.validVertex2;
    }
    public void setValidVertex2(VertexLocation validVertex2) {
        this.validVertex2 = validVertex2;
    }

    public int getRatio() {
        return this.ratio;
    }
    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    public Resource getInputResource() {
        return this.inputResource;
    }
    public void setInputResource(Resource inputResource) {
        this.inputResource = inputResource;
    }

    public EdgeDirection getOrientation() {
        return this.orientation;
    }
    public void setOrientation(EdgeDirection orientation) {
        this.orientation = orientation;
    }
    //endregion

    //region Overrides
    @Override
    public String toString() {
        return "Port{" +
                "ratio=" + ratio +
                ", inputResource=" + inputResource +
                ", validVertex1=" + validVertex1 +
                ", validVertex2=" + validVertex2 +
                ", orientation=" + orientation +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Port)) return false;
        if (!super.equals(o)) return false;

        Port port = (Port) o;

        if (ratio != port.ratio) return false;
        if (inputResource != port.inputResource) return false;
        if (orientation != port.orientation) return false;
        if (validVertex1 != null ? !validVertex1.equals(port.validVertex1) : port.validVertex1 != null) return false;
        if (validVertex2 != null ? !validVertex2.equals(port.validVertex2) : port.validVertex2 != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + ratio;
        result = 31 * result + (inputResource != null ? inputResource.hashCode() : 0);
        result = 31 * result + (validVertex1 != null ? validVertex1.hashCode() : 0);
        result = 31 * result + (validVertex2 != null ? validVertex2.hashCode() : 0);
        result = 31 * result + (orientation != null ? orientation.hashCode() : 0);
        return result;
    }
    //endregion
}
