package com.catan.main.datamodel.player;

public enum Color {

    red(0), orange(1), yellow(2), blue(3), green(4), purple(5), puce(6), white(7), brown(8);

    private int value;

    private Color(int val) {
        this.value = val;
    }

    public int getValue() {
        return this.value;
    }
}
