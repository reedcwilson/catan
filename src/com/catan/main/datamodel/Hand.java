package com.catan.main.datamodel;

import java.io.Serializable;

public abstract class Hand<T extends Enum<T>> implements Serializable {

    public Hand() { }

    //region Abstract Methods
    protected abstract T getInstance();
    public abstract int total();
    public abstract int get(T t);
    public abstract void add(T t, int i);
    //endregion

    //region Protected Methods
    protected Class<T> getTClass() {
        return (Class<T>) getInstance().getClass();
    }
    protected T[] getValues() {
        return getTClass().getEnumConstants();
    }
    //endregion

    //region Operations
    public void set(T t, int count) {
        int diff = count - get(t);
        add(t, diff);
    }
    public void addRange(Hand<T> range) {
        for (T t : getValues()) {
            add(t, range.get(t));
        }
    }
    public void removeRange(Hand<T> range) {
        for (T t : getValues()) {
            add(t, range.get(t) * -1);
        }
    }
    public T removeRandomItem() {
        int random = (int) Math.floor(Math.random() * total());
        return removeItem(random);
    }

    public T removeItem(int random)
    {
        int sum = 0;
        for (T t : getValues()) {
            sum += get(t);
            if (sum > random) {
                add(t, -1);
                return t;
            }
        }
        return null;
    }
    //endregion
}
