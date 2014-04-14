package com.catan.main.persistence.file;

import com.catan.main.datamodel.User;
import com.catan.main.persistence.DataAccessException;

import java.io.File;

public class UserFileCreator extends FileCreator<User> {
    @Override
    public User initialize(File[] files) throws DataAccessException {
        // TODO: deserialize user
        return null;
    }
}
