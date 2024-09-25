package com.zlyntlab.ondabi.chat;

import com.zlyntlab.ondabi.database.Database;

import java.sql.SQLException;

public class Chat {
    // Database
    protected Database database;

    public Chat() {
    String sql = """
            CREATE TABLE IF NOT EXISTS ChatMessage (
                From                varchar(20) PRIMARY KEY,
                ToGroup             varchar(20) PRIMARY KEY,
                To                  varchar(20) PRIMARY KEY,
                Message             TEXT,
                SentDate            varchar(19),
                ReadDate            varchar(19)
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
