package com.catan.main.persistence;

public class ContextCreator {
    private FileContext fileContext;
    private DatabaseContext databaseContext;
    private ContextType contextType;

    public ContextCreator(ContextType contextType) {
        this.contextType = contextType;
    }

    /**
     * returns a DataContext that can be used to persist data
     * @return DataContext
     */
    public DataContext getDataContext() {
        switch (contextType) {
            case FILE:
                return getFileContext();
            case DATABASE:
                return getDatabaseContext();
            default:
                throw new IllegalArgumentException(String.format("Illegal context type: %s", contextType.toString()));
        }
    }

    /**
     * returns a DataContext that can be used to persist data using the given ContextType
     * @param type ContextType
     * @return DataContext
     */
    public DataContext getDataContext(ContextType type) {
        switch (type) {
            case FILE:
                return getFileContext();
            case DATABASE:
                return getDatabaseContext();
            default:
                throw new IllegalArgumentException(String.format("Illegal context type: %s", contextType.toString()));
        }
    }

    private DataContext getDatabaseContext() {
        if (databaseContext == null) {
            databaseContext = new DatabaseContext();
            databaseContext.initialize();
            databaseContext.initializeDataStore();
        }
        return databaseContext;
    }

    private DataContext getFileContext() {
        if (fileContext == null) {
            fileContext = new FileContext();
            fileContext.initialize();
            fileContext.initializeDataStore();
        }
        return fileContext;
    }

    public enum ContextType { FILE, DATABASE }
}
