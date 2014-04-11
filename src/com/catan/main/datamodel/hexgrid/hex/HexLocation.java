package com.catan.main.datamodel.hexgrid.hex;

import com.catan.main.datamodel.hexgrid.edge.EdgeDirection;

import java.io.Serializable;

public class HexLocation implements Serializable {
    public int x;
    public int y;

    public HexLocation() {
    }

    public HexLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }
    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }
    public void setY(int y) {
        this.y = y;
    }

    public HexLocation getLocation(EdgeDirection direction) {
        int z = 0;
        int x;
        int y;
        switch (direction) {
            case NW:
                x = -1;
                y = 0;
                z = -1;
                break;
            case N:
                x = 0;
                y = -1;
                z = -1;
                break;
            case NE:
                x = 1;
                y = -1;
                z = 0;
                break;
            case SE:
                x = 1;
                y = 0;
                z = 1;
                break;
            case S:
                x = 0;
                y = 1;
                z = 1;
                break;
            case SW:
                x = -1;
                y = 1;
                z = 0;
                break;
            default:
                throw new Error("Invalid Direction");
        }
        return new HexLocation(this.x + x, this.y + y);
    }

    @Override
    public String toString() {
        return "HexLocation{" + "x=" + x + ", y=" + y + '}';
    }
    @Override
    public HexLocation clone() {
        return new HexLocation(this.x, this.y);
    }
    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = 31 * result + this.x;
        result = 31 * result + this.y;
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        HexLocation other = (HexLocation) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }
}
