package com.catan.main.persistence.file;

import com.catan.main.persistence.DataAccessException;
import com.catan.main.persistence.ObjectCreator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class FileCreator<T> implements ObjectCreator<T, File[]> {
    @Override
    public List<T> initializeMany(File[] files) throws DataAccessException {
        List<T> objects = new ArrayList<>();
        for (File file : files) {
            objects.add(initialize(new File[] { file }));
        }
        return objects;
    }
}
