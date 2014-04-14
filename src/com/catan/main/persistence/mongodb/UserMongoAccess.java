package com.catan.main.persistence.mongodb;

import com.catan.main.datamodel.PersistenceModel;
import com.catan.main.datamodel.User;
import com.catan.main.persistence.*;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.util.JSON;

public class UserMongoAccess extends UserAccess<MongoOperation> {
    private MongoContext dataContext;
    private UserMongoCreator creator;

    public UserMongoAccess(MongoContext dataContext) {
        this.dataContext = dataContext;
        creator = new UserMongoCreator();
    }

    @Override
    public DataContext getDataContext() {
        return dataContext;
    }

    @Override
    public ObjectCreator getObjectCreator() {
        return creator;
    }

    @Override
    protected MongoOperation getSelectStatement() {
        return new MongoOperation(dataContext.getDb().getCollection("user"));
    }

    @Override
    protected MongoOperation getSingleSelectStatement(int id) throws DataAccessException {
        DBCollection table = dataContext.getDb().getCollection("user");
        BasicDBObject query = new BasicDBObject();
        query.put("_id", id);
        return new MongoOperation(query, table);
    }

    @Override
    protected MongoOperation getInsertStatement(User input) throws DataAccessException {
        DBCollection table = dataContext.getDb().getCollection("user");
        BasicDBObject document = new BasicDBObject();
        document.put("_id", dataContext.getNextSequence("user"));
        document.put("name", input.getName());
        document.put("password", input.getPassword());
        return new MongoOperation(document, null, table);
    }

    @Override
    protected MongoOperation getUpdateStatement(User input) throws DataAccessException {
        DBCollection table = dataContext.getDb().getCollection("user");

        BasicDBObject query = new BasicDBObject();
        query.put("_id", input.getId());

        BasicDBObject document = new BasicDBObject();
        document.put("name", input.getName());
        document.put("password", input.getPassword());

        BasicDBObject obj = new BasicDBObject();
        obj.put("$set", document);

        return new MongoOperation(obj, query, table);
    }

    @Override
    protected MongoOperation getDeleteStatement(User input) throws DataAccessException {
        DBCollection table = dataContext.getDb().getCollection("user");
        BasicDBObject query = new BasicDBObject();
        query.put("_id", input.getId());
        return new MongoOperation(query, table);
    }

    /**
     * checks all of the parameters of the object to verify their validity
     *
     * @param input the input object
     * @return PreparedStatement
     */
    @Override
    protected boolean checkParameters(User input) {
        return DataUtils.checkArgument(input);
    }
}
