package com.catan.main.datamodel;

import com.catan.main.datamodel.commands.Command;
import com.catan.main.persistence.DataAccessException;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

public class CommandHistory implements Serializable {

    //region Fields
    private boolean strict;
    private boolean verbose;
    private ArrayList<Command> commands;
    //endregion

    public CommandHistory(boolean verbose, boolean strict) {
        this.commands = new ArrayList();
        this.strict = strict;
        this.verbose = verbose;
    }

    //region Properties

    public boolean isStrict() {
        return strict;
    }

    public void setStrict(boolean strict) {
        this.strict = strict;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }

    //endregion

    //region Methods
    public String toJSON() {
        return new Gson().toJson(this);
    }

    public boolean execute(DataModel model, Command c) throws DataAccessException {
        this.commands.add(c);
        if (this.verbose) {
            c.log(model);
        }
        c.action(model);
        model.advanceVersion();
        return true;
    }
    public void executeAll(DataModel model) throws DataAccessException {
        for (Command c : this.commands) {
            execute(model, c);
        }
    }
    //endregion

    //region Overrides

    @Override
    public String toString() {
        return "CommandHistory{" +
                "strict=" + strict +
                ", verbose=" + verbose +
                ", commands=" + commands +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommandHistory)) return false;

        CommandHistory that = (CommandHistory) o;

        if (strict != that.strict) return false;
        if (verbose != that.verbose) return false;
        if (commands != null ? !commands.equals(that.commands) : that.commands != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (strict ? 1 : 0);
        result = 31 * result + (verbose ? 1 : 0);
        result = 31 * result + (commands != null ? commands.hashCode() : 0);
        return result;
    }

    //endregion
}
