package edu.csumb.flightapp.model;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String username;
    private String password;

    public User() {}

    @Ignore
    public User(String usr, String pswrd) {
        this.username = usr;
        this.password = pswrd;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "id= " + id + "\nusername= " + username + "\npassword =" + password;
    }
}
