package com.catan.main.persistence.database;

import com.catan.main.datamodel.commands.Command;
import com.catan.main.persistence.DataAccessException;
import com.catan.main.persistence.DataUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommandDatabaseCreator extends DatabaseCreator<Command> {
    @Override
    public Command initialize(ResultSet reader) throws DataAccessException {
        try {
            return (Command)DataUtils.deserialize(reader.getBytes(2));
        } catch (SQLException e) {
            DataUtils.crashOnException(e);
        }
        return null;
    }
}
