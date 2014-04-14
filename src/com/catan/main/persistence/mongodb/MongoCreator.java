package com.catan.main.persistence.mongodb;

import com.catan.main.datamodel.PersistenceModel;
import com.catan.main.persistence.DataAccessException;
import com.catan.main.persistence.ObjectCreator;
import com.mongodb.DBCursor;

import java.util.ArrayList;
import java.util.List;

public abstract class MongoCreator<T extends PersistenceModel> implements ObjectCreator<T, DBCursor> {
    @Override
    public List<T> initializeMany(DBCursor reader) throws DataAccessException {
        ArrayList<T> objects = new ArrayList<>();
        while (reader.hasNext()) {
            objects.add(initialize(reader));
        }
        return objects;
    }
}
