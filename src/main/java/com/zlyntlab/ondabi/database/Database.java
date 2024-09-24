package com.zlyntlab.ondabi.database;

import java.sql.*;

public class Database {
    // Create a singleton
    private static Database instance;
    // Variables
    private Connection connection;

    private Database() throws SQLException{
        this.connection = DriverManager.getConnection("JDBC:sqlite:ondabi_database.db");
        this.connection.setAutoCommit(true);
    }

    public static synchronized Database getInstance() throws SQLException{
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public void createTable(String query) throws SQLException {
        var stmt = this.connection.createStatement();
        stmt.execute(query);
        stmt.close();
    }

    private void prepareParameters(PreparedStatement statement, Object[] params) throws SQLException {
        for(int i = 0; i < params.length; i++){
            Object obj = params[i];

            if(obj.getClass() == String.class) statement.setString(i+1, (String) obj);
            else if(obj.getClass() == Integer.class) statement.setInt(i+1, (Integer) obj);
            else if(obj.getClass() == Double.class) statement.setDouble(i+1, (Double) obj);
            else if(obj.getClass() == Float.class) statement.setFloat(i+1, (Float) obj);
            else if(obj.getClass() == Long.class) statement.setLong(i+1, (Long) obj);

        }
    }

    public void insert(String query, Object... params) throws SQLException{
        // Prepare query
        PreparedStatement statement = this.connection.prepareStatement(query);
        // Prepare parameters
        prepareParameters(statement, params);

        statement.executeUpdate();
    }

    public void delete(String query, Object... params) throws SQLException{
        this.update(query, params);
    }

    public ResultSet select(String query, Object... params) throws  SQLException {
        // Prepare query
        PreparedStatement statement = this.connection.prepareStatement(query);
        // Prepare parameters
        prepareParameters(statement, params);

        return statement.executeQuery();
    }

    public void update(String query, Object... params) throws  SQLException {
        // Prepare query
        PreparedStatement statement = this.connection.prepareStatement(query);
        // Prepare parameters
        prepareParameters(statement, params);

        statement.executeUpdate();
    }

    public void close() throws SQLException {
        this.connection.close();
        this.connection = null;

    }

}