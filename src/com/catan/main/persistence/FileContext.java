package com.catan.main.persistence;

public class FileContext extends DataContext<FileResult, FileCommandStatement> {

    @Override
    public void initialize() {
        
    }

    @Override
    public void initializeDataStore() {

    }

    @Override
    public void startTransaction() {

    }

    @Override
    public void endTransaction(boolean commit) {

    }

    @Override
    public FileResult get(FileCommandStatement fileCommandStatement, int timeout) {
        return null;
    }

    @Override
    public int execute(FileCommandStatement fileCommandStatement, MethodType methodType) {
        return 0;
    }
}
