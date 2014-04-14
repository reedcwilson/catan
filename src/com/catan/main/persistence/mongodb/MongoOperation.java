package com.catan.main.persistence.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

public class MongoOperation {
    private BasicDBObject object;
    private DBCollection table;
    private BasicDBObject query;

    public MongoOperation(DBCollection table) {
        this.table = table;
    }

    public MongoOperation(BasicDBObject query, DBCollection table) {
        this.query = query;
        this.table = table;
    }

    public MongoOperation(BasicDBObject object, BasicDBObject query, DBCollection table) {
        this.object = object;
        this.table = table;
        this.query = query;
    }

    public BasicDBObject getQuery() {
        return query;
    }

    public void setQuery(BasicDBObject query) {
        this.query = query;
    }

    public DBCollection getTable() {
        return table;
    }

    public void setTable(DBCollection table) {
        this.table = table;
    }

    public BasicDBObject getObject() {

        return object;
    }

    public void setObject(BasicDBObject object) {
        this.object = object;
    }
}
