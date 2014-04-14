package com.catan.main.persistence.mongodb;

import com.catan.main.datamodel.commands.Command;
import com.catan.main.datamodel.commands.CommandAdapter;
import com.catan.main.persistence.ContextCreator;
import com.catan.main.persistence.DataAccessException;
import com.catan.main.persistence.DataContext;
import com.catan.main.persistence.ObjectCreator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import java.util.List;

public class CommandMongoCreator extends MongoCreator<Command> {
    @Override
    public Command initialize(DBCursor reader) throws DataAccessException {
        DBObject obj = reader.next();
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeHierarchyAdapter(Command.class, new CommandAdapter());
        Gson gson = builder.create();
        Command command = gson.fromJson(obj.get("command").toString(), Command.class);
        command.setId(Long.parseLong(obj.get("_id").toString()));
        return command;
    }
}
