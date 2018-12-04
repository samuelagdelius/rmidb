package common;

import server.model.File;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface FileCatalogServer extends Remote {

    void register(String username, String password) throws RemoteException, SQLException;

    boolean notValidUsername(String username) throws RemoteException, SQLException;

    UserDTO login(String username, String password, FileCatalogClient remoteNode) throws RemoteException, SQLException;

    void upload(String filename, UserDTO owner, boolean readAndWritable,FileCatalogClient remoteNode) throws RemoteException, SQLException;

    File download (String filename, UserDTO user) throws SQLException,RemoteException;

    void modify(String filename, UserDTO user, String action) throws SQLException, RemoteException;

    List<? extends FileDTO> listAllFiles() throws RemoteException, SQLException;
}
