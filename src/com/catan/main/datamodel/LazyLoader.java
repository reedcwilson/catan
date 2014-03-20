package com.catan.main.datamodel;

import java.util.HashMap;
import java.util.Map;

public abstract class LazyLoader<K, V> {

    private Map<K, V> objects;

    public LazyLoader() {
        this.objects = new HashMap();
    }

    public V get(K key) {
        if (this.objects.get(key) == null) {
            this.objects.put(key, getNewValue());
        }
        return this.objects.get(key);
    }

    protected abstract V getNewValue();
}
