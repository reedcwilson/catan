package com.catan.main.persistence;

public abstract class UserAccess<User> implements IAccess {
    // TODO: we will cache all users here and only go to data store when we have an old copy
}
