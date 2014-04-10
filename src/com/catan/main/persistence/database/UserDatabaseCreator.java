package com.catan.main.persistence.database;

import com.catan.main.datamodel.User;
import com.catan.main.persistence.DataAccessException;

import java.sql.ResultSet;

public class UserDatabaseCreator extends DatabaseCreator<User> {
    @Override
    public User initialize(ResultSet reader) throws DataAccessException {
        return null;
    }
}
