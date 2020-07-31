package edu.csumb.flightapp.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
@Entity
public class LogRecord {
    public static final  int LOG_NEWUSER = 1;
    public static final  int LOG_RESERVE = 2;
    public static final  int LOG_CANCEL = 3;

    @PrimaryKey(autoGenerate = true)
    private long id;

    private int transaction_type;
    private String username;
    private String description;
    private String datetime;

    public LogRecord(){}

    @Ignore
    public LogRecord(int trans_t, String desc, String user){
        this.transaction_type = trans_t;
        this.description = desc;
        this.username = user;
        long t = System.currentTimeMillis();
        String d = new Date(t).toString();
        this.datetime = d;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(int transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return "id= " + id + "/Type= " + typeString() +"/Time= " + datetime+
                "\nUsername= "+ username+
                "\nDescription= " + description;
    }
    private String typeString(){
        switch (transaction_type){
            case 1: return "NEW_USER";
            case 2: return "RESERVE";
            case 3: return "CANCEL";
            default:return "INVALID";
        }
    }
}
