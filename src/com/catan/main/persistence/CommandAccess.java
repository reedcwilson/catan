package com.catan.main.persistence;

import com.catan.main.datamodel.commands.Command;

public abstract class CommandAccess<ResultObject, PreparedStatement> extends DataAccess<Command, ResultObject, PreparedStatement> {
}
