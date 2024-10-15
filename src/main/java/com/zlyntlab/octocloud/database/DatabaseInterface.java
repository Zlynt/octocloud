package com.zlyntlab.octocloud.database;

import java.sql.*;

interface DatabaseInterface {

    public void createTable(String query) throws SQLException;

    public void insert(String query, Object... params) throws SQLException;

    public void delete(String query, Object... params) throws SQLException;

    public ResultSet select(String query, Object... params) throws  SQLException;

    public void update(String query, Object... params) throws  SQLException;

    public void close() throws SQLException;

}
