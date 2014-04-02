package com.catan.main.persistence;

public class ContextFactory {
    private FileContext fileContext;
    private DatabaseContext databaseContext;
    private ContextType contextType;

    public ContextFactory(ContextType contextType) {
        this.contextType = contextType;
    }

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

    enum ContextType { FILE, DATABASE }
}
