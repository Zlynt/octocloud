package com.zlyntlab.ondabi.chat;

import com.zlyntlab.ondabi.database.Database;

import java.sql.SQLException;

public class Group {
    // Database
    protected Database database;

    public Group() {
    String sql = """
            CREATE TABLE IF NOT EXISTS Group (
                GroupName           varchar(20) PRIMARY KEY,
                User                varchar(20) PRIMARY KEY
            );
        """;

        try{
            this.database = Database.getInstance();
            this.database.createTable(sql);
    }catch(
    SQLException exception){
        System.err.println("Cannot connect to the database.");
        System.err.println(exception);
    }
}
}
