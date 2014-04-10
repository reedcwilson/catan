package com.catan.main.datamodel;

import com.catan.main.persistence.DataUtils;

import java.io.Serializable;

public class User implements PersistenceModel, Serializable {

    //region Fields
    private String authentication;
    private String name;
    private String password;
    private Long playerID;
    //endregion

    public User(String name, String password, Long id) {
        this.name = name;
        this.password = password;
        this.playerID = id;
    }
    public User(String username, String password) {
        this.name = username;
        this.password = password;
    }
    public User(String username, String password, Long id, String authentication) {
        this(username, password, id);
        this.authentication = authentication;
    }

    //region Properties
    public String getAuthentication() {
        return this.authentication;
    }
    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Long getId() {
        return this.playerID;
    }
    @Override
    public void setId(Long playerID) {
        this.playerID = playerID;
    }

    @Override
    public byte[] getBytes() {
        return DataUtils.serialize(this);
    }
    //endregion

    //region Methods
    public void generateAuthentication() {
        this.authentication = Integer.valueOf(hashCode()).toString();
    }
    //endregion

    //region Overrides

    @Override
    public String toString() {
        return "User{" +
                "authentication='" + authentication + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", playerID=" + playerID +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User that = (User) o;

        if (authentication != null ? !authentication.equals(that.authentication) : that.authentication != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (playerID != null ? !playerID.equals(that.playerID) : that.playerID != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = authentication != null ? authentication.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (playerID != null ? playerID.hashCode() : 0);
        return result;
    }

    //endregion
}
