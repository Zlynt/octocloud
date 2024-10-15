package com.zlyntlab.octocloud.password_manager;

import com.zlyntlab.octocloud.database.Database;
import com.zlyntlab.octocloud.users.User;

import java.sql.SQLException;

public class Password {
    private Database database;

    public Password() {
        String sql = """
            CREATE TABLE IF NOT EXISTS "Password" (
                "Title"     TEXT,
                "Username" TEXT,
                "Password" TEXT,
                "Folder" TEXT,
                "Note" TEXT,
                "Owner" varchar(20)
            );
        """;

        try{
            this.database = Database.getInstance();
            this.database.createTable(sql);
        }catch(SQLException exception){
            System.err.println("Cannot connect to the database.");
            System.err.println(exception);
        }
    }

    public static void createPassword(String title, String username, String password, String folder, String note, User owner) throws SQLException {
        // Create DB if does not exist
        new Password();

        String sql = """
        INSERT INTO "Password" (
            "Title", "Username", "Password", "Folder", "Note", "Owner"
        ) VALUES (
            ?, ?, ?, ?, ?, ?
        );
        """;

        Database database = Database.getInstance();
        database.insert(sql,
            title, username, password, folder, note, owner
        );



    }
}
