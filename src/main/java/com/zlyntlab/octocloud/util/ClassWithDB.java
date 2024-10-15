package com.zlyntlab.octocloud.util;

import com.zlyntlab.octocloud.database.Database;

import java.sql.SQLException;

public class ClassWithDB {
    // Database
    protected Database database;

    public ClassWithDB() throws SQLException {
        this.database = Database.getInstance();
    }

    protected void createDB(String sql) throws SQLException {
        this.database.createTable(sql);
    }
}
