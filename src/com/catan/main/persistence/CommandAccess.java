package com.catan.main.persistence;

import com.catan.main.datamodel.commands.Command;
import com.catan.main.datamodel.game.Game;

import java.util.List;

public abstract class CommandAccess<PreparedStatement> extends DataAccess<Command, PreparedStatement> {
    /**
     * executes any commands saved in the commands table of the database, but not reflected in the game table
     *
     * @param game the game object to be fast forwarded
     */
    public void fastForward(Game game) throws DataAccessException {
        if (game.getCommandIndex() != -1) {
            List<Command> commands = getCommandsForGame(game.getId());
            for (int i = game.getCommandIndex(); i < commands.size(); i++) {
                commands.get(i).execute(game.getModel());
            }
            game.setCommandIndex(-1);
        }
    }

    public List<Command> getCommandsForGame(Long gameId) throws DataAccessException {
        setIsDirty(true);
        return getDataContext().get(getCommandsForGameStatement(gameId), getObjectCreator());
    }

    protected abstract PreparedStatement getCommandsForGameStatement(Long gameId) throws DataAccessException;
}
