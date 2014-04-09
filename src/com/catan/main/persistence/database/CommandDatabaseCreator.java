package com.catan.main.persistence.database;

import com.catan.main.datamodel.commands.Command;
import com.catan.main.persistence.DataAccessException;
import com.catan.main.persistence.ObjectCreator;

import java.sql.ResultSet;
import java.util.List;

public class CommandDatabaseCreator implements ObjectCreator<Command, ResultSet> {
    @Override
    public Command initialize(ResultSet reader) throws DataAccessException {
        return null;
    }

    @Override
    public List<Command> initializeMany(ResultSet reader) throws DataAccessException {
        return null;
    }
}
