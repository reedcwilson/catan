package com.catan.main.datamodel.hexgrid.edge;

import com.catan.main.datamodel.hexgrid.base.Direction;
import com.catan.main.datamodel.hexgrid.vertex.VertexDirection;

public enum EdgeDirection implements Direction<EdgeDirection> {
    NW(0), N(1), NE(2), SE(3), S(4), SW(5);

    public int value;

    private EdgeDirection(int number) {
        this.value = number;
    }

    private static EdgeDirection numberToDirection(int num) {
        switch (num) {
            case 0:
                return NW;
            case 1:
                return N;
            case 2:
                return NE;
            case 3:
                return SE;
            case 4:
                return S;
            case 5:
                return SW;
        }
        throw new RuntimeException("Invalid direction");
    }

    @Override
    public EdgeDirection rotate(int amount) {
        return numberToDirection((this.value + amount + 6) % 6);
    }

    @Override
    public VertexDirection getVertex(int amount) {
        assert (amount != 0);
        int begin = VertexDirection.E.value;
        int goal = this.value;
        int diff = goal - begin;
        return VertexDirection.E.rotate(diff + amount + (amount < 0 ? 1 : 0));
    }

    @Override
    public EdgeDirection getEdge(int amount) {
        return rotate(amount);
    }
}
