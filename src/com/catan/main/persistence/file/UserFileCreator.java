package com.catan.main.persistence.file;

import com.catan.main.datamodel.User;
import com.catan.main.persistence.DataAccessException;
import com.catan.main.persistence.DataUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

public class UserFileCreator extends FileCreator<User> {
    @Override
    public User initialize(File[] files) throws DataAccessException {
        try {
            byte[] bytes = DataUtils.readFromStream(new BufferedInputStream(new FileInputStream(files[0])));
            User user =  (User) DataUtils.deserialize(bytes);
            user.setId(Long.parseLong(files[0].getName()));
            return user;
        } catch (Exception e) {
            DataUtils.crashOnException(e);
        }
        return null;
    }
}
