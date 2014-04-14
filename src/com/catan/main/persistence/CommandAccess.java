package com.catan.main.persistence;

import com.catan.main.datamodel.commands.Command;
import com.catan.main.datamodel.game.Game;

import java.util.List;

public abstract class CommandAccess<PreparedStatement> extends DataAccess<Command, PreparedStatement>
{
     /**
     * executes any commands saved in the commands table of the database, but not reflected in the game table
     *
     * @param game the game object to be fast forwarded
     */
    public void fastForward(Game game) throws DataAccessException
    {
        List<Command> commands = getObjects();
        int cur = game.getCommandIndex();
        if(cur != -1) {
            for (int i = cur; i < commands.size(); i++) {
                commands.get(i).execute(game.getModel());
                //System.out.println(commands.get(i).getType());
            }
            game.setCommandIndex(-1);
        }
    }
}
