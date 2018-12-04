package server.integration;

import common.FileCatalogClient;
import common.UserDTO;
import server.model.User;

import java.sql.*;

public class UserDAO {
    Connection connection;

    public UserDAO() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/hw3", "root", "");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void register (String username, String password) throws SQLException {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        statement.setString(2, password);
        statement.executeUpdate();
        statement.close();
    }

    public UserDTO login (String username, String password, FileCatalogClient remoteNode) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT username FROM users WHERE username = '" + username + "' AND password = '" + password + "'");
        if(result.next()) {
            return new User(username, remoteNode);
        }
        return null;
    }

    public boolean notValidUsername (String username) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT username FROM users");
        while (result.next()) {
            if (username.equals(result.getString("username"))) {
                return true;
            }
        }
        return false;
    }
}
