package com.catan.main.persistence.file;

import com.catan.main.datamodel.User;
import com.catan.main.datamodel.commands.Command;
import com.catan.main.persistence.DataAccessException;
import com.catan.main.persistence.DataUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

public class CommandFileCreator extends FileCreator<Command> {
    @Override
    public Command initialize(File[] files) throws DataAccessException {
        try {
            byte[] bytes = DataUtils.readFromStream(new BufferedInputStream(new FileInputStream(files[0])));
            Command command =  (Command) DataUtils.deserialize(bytes);
            command.setId(Long.parseLong(files[0].getName()));
            return command;
        } catch (Exception e) {
            DataUtils.crashOnException(e);
        }
        return null;
    }
}
