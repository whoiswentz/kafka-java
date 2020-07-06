package io.wentz;

import java.sql.*;

public class LocalDatabase {

    final private Connection connection;

    public LocalDatabase(String name) throws SQLException {
        String url = "jdbc:sqlite:target/" + name + ".db";
        connection = DriverManager.getConnection(url);
    }

    public boolean createIfNotExists(String sql) {
        try {
            return connection.createStatement().execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean update(String s, String... params) throws SQLException {
        return prepare(s, params).execute();
    }

    public ResultSet query(String s, String... params) throws SQLException {
        return prepare(s, params).executeQuery();
    }

    private PreparedStatement prepare(String s, String... params) throws SQLException {
        var statement = connection.prepareStatement(s);
        for (int i = 0; i < params.length; i++) {
            statement.setString(i + 1, params[i]);
        }
        return statement;
    }

    public void close() throws SQLException {
        connection.close();
    }
}
