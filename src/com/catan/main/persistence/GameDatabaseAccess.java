package com.catan.main.persistence;

import com.catan.main.datamodel.game.Game;

import java.util.List;

public class GameDatabaseAccess extends GameAccess {
    /**
     * a get method which returns a Game with the given id
     * @param id int the id of the desired Game
     * @return Game
     */
    @Override
    public Game get(int id) {
        return null;
    }

    /**
     * a get method which returns all Games
     * @return List of Game objects
     */
    @Override
    public List<Game> getAll() {
        return null;
    }

    /**
     * an insert method which adds the given Game to database
     * @param game Game the Game to insert
     * @return int the id of the inserted object
     */
    @Override
    public int insert(Game game) {
        return 0;
    }

    /**
     * a delete method which removes the given Game from the database
     * @param game Game the Game to delete
     */
    @Override
    public void delete(Game game) {

    }

    /**
     * an update method which updates the given Game in the database
     * @param game Game the Game to update
     */
    @Override
    public void update(Game game) {

    }
}
