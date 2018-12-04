package server.integration;

import common.FileCatalogClient;
import common.UserDTO;
import server.model.File;
import server.model.User;

import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileDAO {
    Connection connection;
    private HashMap<String, FileCatalogClient> fileOwners = new HashMap();

    public FileDAO() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/hw3", "root", "");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void upload (String filename, UserDTO owner, boolean readAndWritable, FileCatalogClient remoteNode) throws SQLException {
        String query = "INSERT INTO files (filename, size, owner, readwrite) VALUES (?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, filename);
        statement.setInt(2, 30);
        statement.setString(3, owner.getUsername());
        statement.setBoolean(4, readAndWritable);
        statement.executeUpdate();
        statement.close();
        //The filename will be hashed together with the remote node of the client who uploaded
        fileOwners.put(filename, remoteNode);
    }

    public File download(String filename, UserDTO user) throws SQLException, RemoteException {
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM files WHERE filename = '" + filename + "'");
        if (result.next()) {
            //Get the remote node from the hash for the client who uploaded it
            FileCatalogClient fileCatalogClient = fileOwners.get(filename);
            fileCatalogClient.receiveMessage(user.getUsername() + " has downloaded the file: " + filename);
            return new File(result.getString("filename"), result.getInt("size"), new User(result.getString("owner")), result.getBoolean("readwrite"));
        }
        return null;
    }

    public void modify (String filename, UserDTO user, String action) throws SQLException, RemoteException {
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT owner, readwrite FROM files WHERE filename = '" + filename + "'");
        if(result.next()) {
            String owner = result.getString("owner");
            if(user.getUsername().equals(owner) || result.getBoolean("readwrite") == true) {
                //Get the remote node from the hash for the client who uploaded it
                FileCatalogClient fileCatalogClient = fileOwners.get(filename);
                if(action.equals("readable")) {
                    statement.executeUpdate("UPDATE files SET readwrite = false WHERE filename = '" + filename + "'");
                    action = "updated";
                }
                else if(action.equals("writable")) {
                    statement.executeUpdate("UPDATE files SET readwrite = true WHERE filename = '" + filename + "'");
                    action = "updated";
                }
                else if(action.equals("delete")) {
                    statement.executeUpdate("DELETE FROM files WHERE filename = '" + filename + "'");
                    action = "deleted";
                }
                statement.close();
                //If the owner of the file is the one modifying it, no need to send it to the client
                if(!user.getUsername().equals(owner))
                    fileCatalogClient.receiveMessage(user.getUsername() + " has " + action + " the file: " + filename);
            }
        }
    }

    public List<File> listAllFiles() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM files");
        List<File> files = new ArrayList<>();
        while (result.next()) {
            files.add(new File(result.getString("filename"), result.getInt("size"), new User(result.getString("owner")), result.getBoolean("readwrite")));
        }
        return files;
    }
}
