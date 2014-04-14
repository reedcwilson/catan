package com.catan.main.persistence.file;

import com.catan.main.datamodel.PersistenceModel;

public class FileOperation<T extends PersistenceModel> {
    private String fileName;
    private T object;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T t) {
        this.object = t;
    }

    public FileOperation(String fileName) {
        this.fileName = fileName;
    }

    public FileOperation(String fileName, T t) {
        this.fileName = fileName;
        this.object = t;
    }
}
