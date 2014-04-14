package com.catan.main.persistence;

import com.catan.main.persistence.mongodb.MongoContext;
import com.catan.main.persistence.sqlite.DatabaseContext;
import com.catan.main.persistence.file.FileContext;

public class ContextCreator {
    private static FileContext fileContext;
    private static DatabaseContext databaseContext;
    private static MongoContext mongoContext;

    /**
     * returns a DataContext that can be used to persist data using the given ContextType
     * @param type ContextType
     * @return DataContext
     */
    public static DataContext getDataContext(ContextType type) {
        switch (type) {
            case FILE:
                return getFileContext();
            case SQLITE:
                return getDatabaseContext();
            case MONGO:
                return getMongoContext();
            default:
                throw new IllegalArgumentException(String.format("Illegal context type: %s", type.toString()));
        }
    }

    private static DataContext getDatabaseContext() {
        if (databaseContext == null) {
            databaseContext = new DatabaseContext();
            databaseContext.initialize();
            databaseContext.initializeDataStore();
        }
        return databaseContext;
    }

    private static DataContext getFileContext() {
        if (fileContext == null) {
            fileContext = new FileContext();
            fileContext.initialize();
            fileContext.initializeDataStore();
        }
        return fileContext;
    }

    private static DataContext getMongoContext() {
        if (mongoContext == null) {
            mongoContext = new MongoContext();
            mongoContext.initialize();
            mongoContext.initializeDataStore();
        }
        return mongoContext;
    }

    public enum ContextType { FILE, SQLITE, MONGO }
}
