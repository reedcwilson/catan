package com.catan.main.persistence.sqlite;

import com.catan.main.datamodel.User;
import com.catan.main.persistence.DataAccessException;
import com.catan.main.persistence.DataUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDatabaseCreator extends DatabaseCreator<User> {
    @Override
    public User initialize(ResultSet reader) throws DataAccessException {
        try {
            User user =  new User(reader.getString(2), reader.getString(3), reader.getLong(1));
            user.setId((long)reader.getInt(1));
            return user;
        } catch (SQLException e) {
            DataUtils.crashOnException(e);
        }
        return null;
    }
}
