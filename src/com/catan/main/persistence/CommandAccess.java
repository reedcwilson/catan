package com.catan.main.persistence;

public abstract class CommandAccess<ICommand> implements IAccess {
    // TODO: we will cache all commands here and only go to data store when we have an old copy
}
