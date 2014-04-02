package com.catan.main.persistence;

import com.catan.main.datamodel.commands.Command;

public abstract class CommandAccess implements IAccess<Command> {
    // TODO: we will cache all commands here and only go to data store when we have an old copy
}
