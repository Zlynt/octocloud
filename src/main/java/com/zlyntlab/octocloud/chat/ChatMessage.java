package com.zlyntlab.octocloud.chat;

import com.zlyntlab.octocloud.database.Database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatMessage {
    // Database
    protected Database database;
    // Variables
    private String id;
    private String from;
    private String to;
    private String group;
    private String message;
    private String dateSent;

    public ChatMessage() {
        String sql = """
            CREATE TABLE IF NOT EXISTS "ChatMessage" (
                "From" varchar(20),
                "To" varchar(20),
                "Group" varchar(20),
                "Message" TEXT,
                "DateSent" varchar(19)
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

    public ChatMessage(String id) {
        this();

        this.id = id;

        String query = """
            SELECT *
            FROM "ChatMessage"
            WHERE "rowid" = ?;
        """;

        try {
            ResultSet result = this.database.select(query,
                    this.id
            );
            if (!result.next()) throw new ChatMessageNotFoundException();

            this.from       = result.getString("From");
            this.to         = result.getString("To");
            this.group      = result.getString("Group");
            this.message    = result.getString("Message");
            this.dateSent   = result.getString("DateSent");
        } catch (SQLException e) {
            throw new ChatMessageNotFoundException();
        }
    }

    public ChatMessage(String from, String to, String group, String message, String dateSent) throws SQLException {
        this();

        this.from = from;
        this.to = to;
        this.group = group;
        this.message = message;
        this.dateSent = dateSent;

        String query = """
            SELECT rowid AS ID, * FROM "ChatMessage"
            WHERE
                "From"        = ? AND
                "To"          = ? AND
                "Group"       = ? AND
                "Message"     = ? AND
                "DateSent"    = ?
        """;

        try {
            ResultSet result = this.database.select(query,
                this.from,
                this.to,
                this.group,
                this.message,
                this.dateSent
            );
            if (!result.next()) throw new ChatMessageNotFoundException();
            this.id = result.getString("ID");
        } catch (SQLException e) {

            throw e;
        }
    }

    public static ChatMessage create(String from, String to, String group, String message, String dateSent) throws SQLException {
        // Create DB's table if it does not exist
        new ChatMessage();

        String query = """
        INSERT INTO ChatMessage (
            "From",
            "To",
            "Group",
            "Message",
            "DateSent"
        ) VALUES (?, ?, ?, ?, ?);
        """;

        Database database = Database.getInstance();
        database.insert(query, from, to, group, message, dateSent);

        return new ChatMessage(from, to, group, message, dateSent);
    }

    public String getFrom() {
        return this.from;
    }
    public String getTo() {
        return this.to;
    }
    public String getGroup() {
        return this.group;
    }
    public String getMessage() {
        return this.message;
    }
    public String getDateSent() {
        return this.dateSent;
    }

    public void delete() throws SQLException {
        String query = """
        DELETE FROM "ChatMessage"
        WHERE "rowid" = ?
        """;

        this.database.delete(query, this.id);
    }

}
