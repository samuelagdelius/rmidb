package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FileCatalogClient extends Remote {

    void receiveMessage(String message) throws RemoteException;

}