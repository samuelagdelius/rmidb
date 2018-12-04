package server.controller;

import common.FileCatalogClient;
import common.FileCatalogServer;
import common.FileDTO;
import common.UserDTO;
import server.integration.FileDAO;
import server.integration.UserDAO;
import server.model.File;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;

public class Controller extends UnicastRemoteObject implements FileCatalogServer {
    private final FileDAO fileD;
    private final UserDAO userD;

    public Controller() throws RemoteException {
        this.fileD = new FileDAO();
        this.userD = new UserDAO();
    }

    public void register(String username, String password) throws SQLException {
        userD.register(username,password);
    }

    public boolean notValidUsername (String username) throws SQLException {
        return userD.notValidUsername(username);
    }

    public UserDTO login(String username, String password, FileCatalogClient remoteNode) throws SQLException {
        return userD.login(username, password, remoteNode);
    }

    public void upload (String filename, UserDTO owner, boolean readAndWritable, FileCatalogClient remoteNode) throws SQLException {
        fileD.upload(filename, owner, readAndWritable, remoteNode);
    }

    public File download (String filename, UserDTO user) throws SQLException, RemoteException {
        return fileD.download(filename, user);
    }

    public void modify(String filename, UserDTO user, String action) throws SQLException, RemoteException {
        fileD.modify(filename,user, action);
    }

    public List<? extends FileDTO> listAllFiles() throws SQLException {
        return fileD.listAllFiles();
    }
}
