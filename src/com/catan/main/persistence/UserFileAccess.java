package com.catan.main.persistence;

import com.catan.main.datamodel.User;

import java.util.List;

public class UserFileAccess extends UserAccess {

    /**
     * a get method which returns a User with the given id
     * @param id int the id of the desired User
     * @return User
     */
    @Override
    public User get(int id) {
        return null;
    }

    /**
     * a get method which returns all Users
     * @return List of Users
     */
    @Override
    public List getAll() {
        return null;
    }

    /**
     * an insert method which adds the given User to file system
     * @param user User the User to insert
     * @return int the id of the inserted User
     */
    @Override
    public int insert(User user) {
        return 0;
    }

    /**
     * a delete method which removes the given User from the file system
     * @param user User the User to delete
     */
    @Override
    public void delete(User user) {

    }

    /**
     * an update method which updates the given User in the file system
     * @param user User the User to update
     */
    @Override
    public void update(User user) {

    }
}
