package com.catan.main.persistence.mongodb;

import com.catan.main.datamodel.commands.Command;
import com.catan.main.persistence.*;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.util.JSON;

public class CommandMongoAccess extends CommandAccess<MongoOperation> {

    private MongoContext dataContext;
    private CommandMongoCreator creator;

    public CommandMongoAccess(MongoContext dataContext) {
        this.dataContext = dataContext;
        creator = new CommandMongoCreator();
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
        return new MongoOperation(dataContext.getDb().getCollection("command"));
    }

    @Override
    protected MongoOperation getSingleSelectStatement(int id) throws DataAccessException {
        DBCollection table = dataContext.getDb().getCollection("command");
        BasicDBObject query = new BasicDBObject();
        query.put("_id", id);
        return new MongoOperation(query, table);
    }

    @Override
    protected MongoOperation getInsertStatement(Command input) throws DataAccessException {
        DBCollection table = dataContext.getDb().getCollection("command");
        BasicDBObject document = new BasicDBObject();
        document.put("_id", dataContext.getNextSequence("command"));
        document.put("command", JSON.parse(dataContext.getGson().toJson(input)));
        document.put("game_id", input.getGameId());
        return new MongoOperation(document, null, table);
    }

    @Override
    protected MongoOperation getUpdateStatement(Command input) throws DataAccessException {
        DBCollection table = dataContext.getDb().getCollection("command");

        BasicDBObject query = new BasicDBObject();
        query.put("_id", input.getId());

        BasicDBObject document = new BasicDBObject();
        document.put("command", JSON.parse(dataContext.getGson().toJson(input)));
        document.put("game_id", input.getGameId());

        BasicDBObject obj = new BasicDBObject();
        obj.put("$set", document);

        return new MongoOperation(obj, query, table);
    }

    @Override
    protected MongoOperation getDeleteStatement(Command input) throws DataAccessException {
        DBCollection table = dataContext.getDb().getCollection("command");
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
    protected boolean checkParameters(Command input) {
        return DataUtils.checkArgument(input);
    }

    @Override
    protected MongoOperation getCommandsForGameStatement(Long gameId) throws DataAccessException {
        DBCollection table = dataContext.getDb().getCollection("command");
        BasicDBObject query = new BasicDBObject();
        query.put("game_id", gameId);
        return new MongoOperation(query, table);
    }
}
