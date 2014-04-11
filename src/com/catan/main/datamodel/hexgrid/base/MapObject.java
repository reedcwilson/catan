package com.catan.main.datamodel.hexgrid.base;

import com.catan.main.datamodel.hexgrid.hex.HexLocation;

import java.io.Serializable;

public class MapObject<L extends HexLocation, V extends Value> implements Serializable {
    protected V value;
    protected L location;

    public MapObject(V value, L location) {
        this.value = value;
        this.location = location;
    }

    public L getLocation() {
        return this.location;
    }
    public void setLocation(L location) {
        this.location = (L)location.clone();
    }

    public V getValue() {
        return this.value;
    }
    public void setValue(V value) {
        this.value = value;
    }

    public int hashCode() {
        int result = 1;
        result = 31 * result + (this.location == null ? 0 : this.location.hashCode());
        result = 31 * result + (this.value == null ? 0 : this.value.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MapObject)) {
            return false;
        }
        MapObject other = (MapObject) obj;
        if (this.location == null) {
            if (other.location != null) {
                return false;
            }
        } else if (!this.location.equals(other.location)) {
            return false;
        }
        if (this.value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!this.value.equals(other.value)) {
            return false;
        }
        return true;
    }
}
