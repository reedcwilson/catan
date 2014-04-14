package com.catan.main.persistence.file;

public class FileOperation {
    private String fileName;
    private Object object;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public FileOperation(String fileName) {
        this.fileName = fileName;
    }

    public FileOperation(String fileName, Object object) {
        this.fileName = fileName;
        this.object = object;
    }
}
