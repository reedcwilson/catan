package com.catan.main.persistence;

import com.catan.main.datamodel.User;

public abstract class UserAccess implements IAccess<User> {
    // TODO: we will cache all users here and only go to data store when we have an old copy
}
