package com.catan.main.persistence.mongodb;

import com.catan.main.datamodel.PersistenceModel;
import com.catan.main.persistence.*;
import com.google.gson.Gson;
import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.List;

public class MongoContext<T extends PersistenceModel> extends DataContext<T, MongoOperation> {

    //region Fields
    MongoClient client;
    DB db;
    Gson gson;
    CommandMongoAccess commandAccess;
    UserMongoAccess userAccess;
    GameMongoAccess gameAccess;
    //endregion

    //region Properties
    public DB getDb() {
        return db;
    }

    public Gson getGson() {
        return gson;
    }
    //endregion

    //region Public Interface

    public MongoContext() {
        commandAccess = new CommandMongoAccess(this);
        userAccess = new UserMongoAccess(this);
        gameAccess = new GameMongoAccess(this);
    }

    @Override
    public CommandAccess getCommandAccess() {
        return commandAccess;
    }

    @Override
    public UserAccess getUserAccess() {
        return userAccess;
    }

    @Override
    public GameAccess getGameAccess() {
        return gameAccess;
    }

    /**
     * performs any single use initialization
     */
    @Override
    public void initialize() {
        try {
            client = new MongoClient();
            db = client.getDB("catan");
            gson = new Gson();
            initializeCounters();
        } catch (UnknownHostException e) {
            DataUtils.crashOnException(e);
        }
    }

    /**
     * creates tables / initializes file structure
     */
    @Override
    public void initializeDataStore() {
        System.out.println("no data store initialization required");
    }

    /**
     * Resets the current DataContext
     */
    @Override
    public void reset() {
        db.dropDatabase();
    }

    /**
     * starts a transaction that can be rolled back
     */
    @Override
    public void startTransaction() {
        System.out.println("mongo does not have transactions because almost all operations are atomic but two-phase commits are possible if needed");
    }

    /**
     * ends the current transaction
     *
     * @param commit boolean whether or not to commit the transaction
     */
    @Override
    public void endTransaction(boolean commit) {
        System.out.println("mongo does not have transactions because almost all operations are atomic but two-phase commits are possible if needed");
    }

    /**
     * performs a get operation based on the given command and timeout
     *
     * @param operation query to run
     * @param timeout   length of time before throwing an exception
     * @return ResultSet an object of the parameter type (T)
     */
    @Override
    public List<T> get(MongoOperation operation, int timeout, ObjectCreator creator) throws DataAccessException {
        if (operation.getQuery() == null) {
            return creator.initializeMany(operation.getTable().find());
        } else {
            return creator.initializeMany(operation.getTable().find(operation.getQuery()));
        }
    }

    /**
     * executes the given command based on the methodType
     *
     * @param operation  object to execute method on
     * @param methodType MethodType (update, insert, delete)
     * @return rows affected or id of inserted object
     */
    @Override
    public int execute(MongoOperation operation, MethodType methodType) throws DataAccessException {
        switch (methodType) {
            case INSERT:
                operation.getTable().insert(operation.getObject());
                return (int)operation.getObject().get("_id");
            case UPDATE:
                return operation.getTable().update(operation.getQuery(), operation.getObject()).getN();
            case DELETE:
                return operation.getTable().remove(operation.getObject()).getN();
            case SELECT:
                throw new DataAccessException("cannot execute query from this method");
            default:
                throw new DataAccessException("Did not recognize the method type: " + methodType);
        }
    }

    public int getNextSequence(String collection) {
        String sequence_field = "sequence";
        DBCollection counters = db.getCollection("counters");

        DBObject query = new BasicDBObject();
        query.put("_id", collection + "_id");

        DBObject change = new BasicDBObject(sequence_field, 1);
        DBObject update = new BasicDBObject("$inc", change);

        DBObject res = counters.findAndModify(query, new BasicDBObject(), new BasicDBObject(), false, update, true, true);
        return Integer.parseInt(res.get(sequence_field).toString());
    }

    //endregion

    //region Helper Methods
    private void initializeCounters() {
        DBCollection collection = db.getCollection("counters");

        DBCollection users = db.getCollection("user");
        if (users == null) {
            BasicDBObject user_id = new BasicDBObject();
            user_id.put("_id", "user_id");
            user_id.put("sequence", 1);
            collection.insert(user_id);
        }

        DBCollection games = db.getCollection("game");
        if (games == null) {
            BasicDBObject game_id = new BasicDBObject();
            game_id.put("_id", "game_id");
            game_id.put("sequence", 1);
            collection.insert(game_id);
        }

        DBCollection commands = db.getCollection("command");
        if (commands == null) {
            BasicDBObject command_id = new BasicDBObject();
            command_id.put("_id", "command_id");
            command_id.put("sequence", 1);
            collection.insert(command_id);
        }

    }
    //endregion

}
