package com.zlyntlab.octocloud.chat;

import com.zlyntlab.octocloud.database.Database;
import com.zlyntlab.octocloud.users.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

//@RestController
//@RequestMapping("/api/msg")
public class ChatHistory {
    // Database
    protected Database database;
    // User that owns this message
    private User toUser;
    // Message list
    private ArrayList<ChatMessage> messageHistory;

    public ChatHistory() {
        // Create table if it does not exist
        new ChatMessage();
        // Initialize HashMap
        this.messageHistory = new ArrayList<ChatMessage>();

        // Load Database
        try {
            this.database = Database.getInstance();
        } catch(SQLException exception) {
            System.err.println("Cannot connect to the database.");
            System.err.println(exception);
        }
    }

    public ChatHistory(String toUsername) {
        this();

        this.toUser = new User(toUsername);

        String query = """
        SELECT rowid AS ID FROM ChatMessage
        WHERE To = ? or From = ?
        """;

        try {
            String username = this.toUser.getUsername();
            ResultSet result = this.database.select(query, username, username);

            while (result.next()) {
                ChatMessage message = new ChatMessage(result.getString("ID"));
                messageHistory.add(message);
            }

        } catch (SQLException e) {
            throw new ChatMessageNotFoundException();
        }
    }

    public ChatHistory(String toUsername, String group) {
        this();

        // Verify if user exists
        this.toUser = new User(toUsername);

        String query = """
        SELECT rowid AS ID FROM "ChatMessage"
        WHERE ( "To" = ? OR "From" = ? ) AND Group = ?
        """;

        try {
            String username = this.toUser.getUsername();
            ResultSet result = this.database.select(query, username, username, group);

            while (result.next()) {
                ChatMessage message = new ChatMessage(result.getString("ID"));
                messageHistory.add(message);
            }

        } catch (SQLException e) {
            throw new ChatMessageNotFoundException();
        }
    }

    public ArrayList<ChatMessage> getMessageHistory() {
        return this.messageHistory;
    }
}
