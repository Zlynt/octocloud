package com.zlyntlab.ondabi.users;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.zlyntlab.ondabi.crypto.Argon2;
import com.zlyntlab.ondabi.database.Database;

public class User{
    // Database
    private Database database;
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
            CREATE TABLE IF NOT EXISTS Users (
                Username            varchar(20),
                Password            varchar(30),

                PublicEncryptionKey varchar(255),
                privateEncryptionKey varchar(255),

                PublicSignatureKey varchar(255),
                PrivateSignatureKey varchar(255)
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

    public User(String username) {
        this();

        this.username = username;

        try {
            ResultSet result = this.database.select("SELECT * from Users WHERE Username = ?;", this.username);
            if (result.next() == false){
                System.out.println("User "+username+" is not in the database. Creating...");
                String insert_sql = """
                INSERT INTO Users (Username) VALUES (?);
                """;
                this.database.insert(insert_sql, username);
            }
        } catch(SQLException ex){
            System.err.println(ex);
        }
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
            ResultSet result = this.database.select("SELECT Password from Users WHERE Username = ?;", this.username);
            if (result.next() == true){
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

    public void setPrivateKey(String privateKey){

    }

    public void isPasswordCorrect(String hashedPassword) {

    }
}