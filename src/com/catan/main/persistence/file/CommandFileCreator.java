package com.catan.main.persistence.file;

import com.catan.main.datamodel.commands.Command;
import com.catan.main.persistence.DataAccessException;

import java.io.File;

public class CommandFileCreator extends FileCreator<Command> {
    @Override
    public Command initialize(File[] files) throws DataAccessException {
        // TODO: deserialize command
        return null;
    }
}
