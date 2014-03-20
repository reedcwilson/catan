package com.catan.main.datamodel.hexgrid.vertex;

import com.catan.main.datamodel.hexgrid.base.Direction;
import com.catan.main.datamodel.hexgrid.edge.EdgeDirection;

public enum VertexDirection implements Direction<VertexDirection> {
    W(0), NW(1), NE(2), E(3), SE(4), SW(5);

    public int value;

    private VertexDirection(int number) {
        this.value = number;
    }

    private static VertexDirection numberToDirection(int number) {
        switch (number) {
            case 0:
                return W;
            case 1:
                return NW;
            case 2:
                return NE;
            case 3:
                return E;
            case 4:
                return SE;
            case 5:
                return SW;
        }
        throw new RuntimeException("Number out of bound for vertex direction");
    }

    public VertexDirection rotate(int amount) {
        return numberToDirection((this.value + amount + 6) % 6);
    }
    public EdgeDirection getEdge(int amount) {
        assert (amount != 0);
        int begin = EdgeDirection.N.value;
        int goal = this.value;
        int diff = goal - begin;
        return EdgeDirection.N.rotate(diff + amount + (amount > 0 ? -1 : 0));
    }
    public VertexDirection getVertex(int amount) {
        return rotate(amount);
    }
}
