package com.catan.main.datamodel;

import com.catan.main.datamodel.commands.Command;
import com.google.gson.Gson;

import java.util.ArrayList;

public class CommandHistory {

    //region Fields
    private boolean strict;
    private boolean verbose;
    private ArrayList<Command> commands;
    //endregion

    public CommandHistory(boolean isVerbose, boolean isStrict) {
        this.strict = isStrict;
        this.verbose = isVerbose;
        this.commands = new ArrayList();
    }

    //region Properties
    public boolean isVerbose() {
        return this.verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
    public boolean isStrict() {
        return this.strict;
    }

    public void setStrict(boolean strict) {
        this.strict = strict;
    }
    public ArrayList<Command> getCommands() {
        return this.commands;
    }

    //endregion

    //region Methods
    public String toJSON() {
        return new Gson().toJson(this);
    }

    public boolean execute(DataModel model, Command c) {
        this.commands.add(c);
        if (this.verbose) {
            c.log(model);
        }
        c.doExecute(model);
        model.advanceVersion();
        return true;
    }
    public void executeAll(DataModel model) {
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
