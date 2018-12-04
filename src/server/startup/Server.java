package server.startup;

import server.controller.Controller;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;

public class Server {
    public static void main(String[] args) {
        try {
            new Server().startRegistry();
            Naming.bind("FileCatalog", new Controller());
            System.out.println("FileCatalog server started...");
        } catch (RemoteException | MalformedURLException | AlreadyBoundException e) {
            System.out.println("Failed to start the FileCatalog server");
            e.printStackTrace();
        }
    }

    private void startRegistry() throws RemoteException {
        try {
            LocateRegistry.getRegistry().list();
        } catch (RemoteException noRegistryIsRunning) {
            //Create our registry if we cant locate it
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        }
    }
}
