package com.zlyntlab.ondabi.users;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.zlyntlab.ondabi.crypto.Argon2;
import com.zlyntlab.ondabi.database.Database;

public class User {
    // Database
    protected Database database;
    // User variables
    private String username;
    // Encryption key
    private String publicEncryptionKey;
    private String privateEncryptionKey;
    // Signature key
    private String publicSignatureKey;
    private String privateSignatureKey;

    private User() {
        String sql = """
            CREATE TABLE IF NOT EXISTS "User" (
                "Username"                varchar(20) PRIMARY KEY,
                "Password"                varchar(30),

                "PublicEncryptionKey"     varchar(255),
                "privateEncryptionKey"    varchar(255),

                "PublicSignatureKey"      varchar(255),
                "PrivateSignatureKey"     varchar(255),
                "IsAdmin"                 INTEGER NOT NULL DEFAULT 0
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

    public User(String username) throws UserNotFoundException {
        this();

        this.username = username;

        try {
            ResultSet result = this.database.select("SELECT * from User WHERE Username = ?;", this.username);
            if (!result.next()) throw new UserNotFoundException(username);
        } catch (SQLException e) {
            throw new UserNotFoundException(username);
        }
    }

    public static User create(String username) throws SQLException {
        // Create DB Table
        User user = new User();

        // Insert into database the new user
        String insert_sql = """
                INSERT INTO Users (Username) VALUES (?);
        """;
        Database database = Database.getInstance();
        database.insert(insert_sql, username);

        return new User(username);
    }

    public static User create(String username, String password) throws SQLException {
        User user = create(username);
        user.setPassword(password);

        return user;
    }

    public Boolean delete() throws SQLException, UserNotFoundException {
        String sql = """
            DELETE FROM User
            WHERE Username = ?;
        """;
        this.database.delete(sql, username);

        return true;
    }

    public String getUsername() {
        return this.username;
    }

    public void setPassword(String password) {
        String query = """
        UPDATE Users
        SET Password = ?
        WHERE Username = ?
        """;
        Argon2 argon2 = new Argon2();
        String hashedPassword = argon2.hash(password);
        try {
            this.database.update(query, hashedPassword, this.username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private String getPassword(){
        try {
            ResultSet result = this.database.select("SELECT Password from User WHERE Username = ?;", this.username);
            if (result.next()){
                return result.getString("Password");
            }
            throw new RuntimeException("Password not found");
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    public Boolean checkPassword(String password) {
        Argon2 argon2 = new Argon2();
        return argon2.verify(password, this.getPassword());
    }

    public boolean isAdmin() {
        String query = """
        SELECT IsAdmin
        FROM User
        WHERE Username = ?;
        """;
        try {
            ResultSet resultSet = this.database.select(query, this.username);
            if (resultSet.next()){
                return resultSet.getInt("IsAdmin") == 1;
            }
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
        return false;
    }

    public void setAdmin(boolean isAdmin) {
        String query = """
        UPDATE Users
        SET IsAdmin = ?
        WHERE Username = ?
        """;
        int parameter = isAdmin ? 1 : 0;
        try {
            this.database.update(query, parameter, this.username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}