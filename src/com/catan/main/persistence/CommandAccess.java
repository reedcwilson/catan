package com.catan.main.persistence;

import com.catan.main.datamodel.commands.Command;

public abstract class CommandAccess<PreparedStatement> extends DataAccess<Command, PreparedStatement> {
}
