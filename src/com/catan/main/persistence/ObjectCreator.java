package com.catan.main.persistence;

import java.util.List;

public interface ObjectCreator<T, ResultObject> {

    /**
     * initializes an object of type T with given resultSet
     *
     * @param reader the reader
     * @return T
     * @throws DataAccessException
     */
    T initialize(ResultObject reader)
            throws DataAccessException;

    /**
     * initializes an list of objects of type T with given resultSet
     *
     * @param reader the reader
     * @return T the resulting list
     * @throws DataAccessException
     */
    List<T> initializeMany(ResultObject reader)
            throws DataAccessException;
}
