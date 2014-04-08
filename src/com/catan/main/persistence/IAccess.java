package com.catan.main.persistence;

import java.util.List;

public interface IAccess<T>{
    /**
     * a get method which returns an object with the given id
     * @param id int the id of the desired object
     * @return T the object
     */
    T get(int id) throws DataAccessException;

    /**
     * a get method which returns all objects of type T
     * @return List of T objects
     */
    List<T> getAll() throws DataAccessException;

    /**
     * an insert method which adds the given T object to datastore
     * @param t T the object to insert
     * @return int the id of the inserted object
     */
    int insert(T t) throws DataAccessException;

    /**
     * a delete method which removes the given object from the datastore
     * @param t T the object to delete
     */
    void delete(T t) throws DataAccessException;

    /**
     * an update method which updates the given object in the datastore
     * @param t T the object to update
     */
    void update(T t) throws DataAccessException;

    /**
     * Gets the class's dataContext
     * @return DataContext
     */
    DataContext getDataContext();

    /**
     * returns true if database has been changed false otherwise
     * @return true of false
     */
    boolean isDirty();
}
