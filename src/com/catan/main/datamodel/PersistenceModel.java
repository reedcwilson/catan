package com.catan.main.datamodel;

public interface PersistenceModel {
    Long getId();
    void setId(Long id);
    byte[] getBytes();
}
