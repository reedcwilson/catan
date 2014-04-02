package com.catan.main.persistence;

public interface IAccess<T>{
    T get();
    long insert(T t);
    void delete(T t);
    void update(T t);
}
