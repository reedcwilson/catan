package com.catan.main.persistence.mongodb;

import com.catan.main.datamodel.PersistenceModel;
import com.catan.main.datamodel.commands.Command;
import com.catan.main.datamodel.game.Game;
import com.catan.main.persistence.*;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.util.JSON;

public class GameMongoAccess extends GameAccess<MongoOperation> {
    private MongoContext dataContext;
    private GameMongoCreator creator;

    public GameMongoAccess(MongoContext dataContext) {
        this.dataContext = dataContext;
        creator = new GameMongoCreator();
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
        return new MongoOperation(dataContext.getDb().getCollection("game"));
    }

    @Override
    protected MongoOperation getSingleSelectStatement(int id) throws DataAccessException {
        DBCollection table = dataContext.getDb().getCollection("game");
        BasicDBObject query = new BasicDBObject();
        query.put("_id", id);
        return new MongoOperation(query, table);
    }

    @Override
    protected MongoOperation getInsertStatement(Game input) throws DataAccessException {
        DBCollection table = dataContext.getDb().getCollection("game");
        BasicDBObject document = new BasicDBObject();
        Command command = getLatestCommand();
        document.put("_id", dataContext.getNextSequence("game"));
        if (command == null)
            document.put("commandIndex", -1);
        else
            document.put("commandIndex", command.getId());
        document.put("currentBlob", JSON.parse(dataContext.getGson().toJson(input)));
        document.put("originalBlob", JSON.parse(dataContext.getGson().toJson(input)));
        return new MongoOperation(document, null, table);
    }

    @Override
    protected MongoOperation getUpdateStatement(Game input) throws DataAccessException {
        DBCollection table = dataContext.getDb().getCollection("game");

        BasicDBObject query = new BasicDBObject();
        query.put("_id", input.getId());

        BasicDBObject document = new BasicDBObject();
        document.put("commandIndex", getLatestCommand());
        document.put("currentBlob", JSON.parse(dataContext.getGson().toJson(input)));

        BasicDBObject obj = new BasicDBObject();
        obj.put("$set", document);

        return new MongoOperation(obj, query, table);
    }

    @Override
    protected MongoOperation getDeleteStatement(Game input) throws DataAccessException {
        DBCollection table = dataContext.getDb().getCollection("game");
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
    protected boolean checkParameters(Game input) {
        return DataUtils.checkArgument(input);
    }

    private Command getLatestCommand() throws DataAccessException {
        DBCollection commands = dataContext.getDb().getCollection("command");
        DBCursor cursor = commands.find().sort(new BasicDBObject("_id", -1)).limit(1);
        if (cursor.hasNext()) {
            return new CommandMongoCreator().initialize(cursor);
        }
        return null;
    }
}
