package com.catan.main.persistence.database;

import com.catan.main.datamodel.commands.Command;
import com.catan.main.persistence.DataAccessException;

import java.sql.ResultSet;

public class CommandDatabaseCreator extends DatabaseCreator<Command> {
    @Override
    public Command initialize(ResultSet reader) throws DataAccessException {
        return null;
    }
}
