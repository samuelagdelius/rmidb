package client.startup;

import client.view.View;
import common.FileCatalogServer;

import java.rmi.Naming;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Main {
    public static void main(String[] args) {
        try {
            FileCatalogServer fcat = (FileCatalogServer) Naming.lookup("FileCatalog");
            new View().start(fcat);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            System.out.println("Failed to start the FileCatalog client");
        }
    }
}
