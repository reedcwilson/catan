package com.catan.main.persistence;

import com.catan.main.datamodel.commands.Command;

import java.util.List;

public class CommandFileAccess extends CommandAccess {
    
    /**
     * a get method which returns a Command with the given id
     * @param id int the id of the desired Command
     * @return Command
     */
    @Override
    public Command get(int id) {
        return null;
    }

    /**
     * a get method which returns all Command
     * @return List of Command objects
     */
    @Override
    public List<Command> getAll() {
        return null;
    }

    /**
     * an insert method which adds the given Command object to file system
     * @param command Command the object to insert
     * @return int the id of the inserted object
     */
    @Override
    public int insert(Command command) {
        return 0;
    }

    /**
     * a delete method which removes the given Command from the file system
     * @param command Command the object to delete
     */
    @Override
    public void delete(Command command) {

    }

    /**
     * an update method which updates the given Command in the file system
     * @param command Command the object to update
     */
    @Override
    public void update(Command command) {

    }
}
