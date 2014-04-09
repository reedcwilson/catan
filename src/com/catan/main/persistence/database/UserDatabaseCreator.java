package com.catan.main.persistence.database;

import com.catan.main.datamodel.User;
import com.catan.main.persistence.DataAccessException;
import com.catan.main.persistence.ObjectCreator;

import java.sql.ResultSet;
import java.util.List;

public class UserDatabaseCreator implements ObjectCreator<User, ResultSet> {
    @Override
    public User initialize(ResultSet reader) throws DataAccessException {
        return null;
    }

    @Override
    public List<User> initializeMany(ResultSet reader) throws DataAccessException {
        return null;
    }
}
