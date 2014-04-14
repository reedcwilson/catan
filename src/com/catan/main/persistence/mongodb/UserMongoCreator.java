package com.catan.main.persistence.mongodb;

import com.catan.main.datamodel.User;
import com.catan.main.datamodel.game.Game;
import com.catan.main.persistence.ContextCreator;
import com.catan.main.persistence.DataAccessException;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class UserMongoCreator extends MongoCreator<User> {
    @Override
    public User initialize(DBCursor reader) throws DataAccessException {
        DBObject obj = reader.next();
        User user = new User(obj.get("name").toString(), obj.get("password").toString());
        user.setId(Long.parseLong(obj.get("_id").toString()));
        return user;
    }
}
