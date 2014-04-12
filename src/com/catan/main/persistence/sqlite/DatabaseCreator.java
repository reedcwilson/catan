package com.catan.main.persistence.sqlite;

import com.catan.main.persistence.DataAccessException;
import com.catan.main.persistence.DataUtils;
import com.catan.main.persistence.ObjectCreator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class DatabaseCreator<T> implements ObjectCreator<T, ResultSet> {
    @Override
    public List<T> initializeMany(ResultSet reader) throws DataAccessException {
        List<T> commands = new ArrayList<>();
        try {
            while (reader.next()) {
                commands.add(initialize(reader));
            }
        } catch (SQLException e) {
            DataUtils.crashOnException(e);
        }
        return commands;
    }
}
