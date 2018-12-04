package client.view;

import common.FileCatalogClient;
import common.FileCatalogServer;
import common.FileDTO;
import common.UserDTO;
import server.model.File;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class View implements Runnable {
    private FileCatalogServer fileCatalogServer;
    private FileCatalogClient myRemoteObject;
    private boolean running = false;
    private UserDTO user = null;

    public void start(FileCatalogServer fileCatalogServer) throws RemoteException {
        this.fileCatalogServer = fileCatalogServer;
        this.myRemoteObject = new ServerMessages();
        if(running)
            return;
        running = true;
        new Thread(this).start();
    }

    @Override
    public void run() {
        while(running) {
            try {
                String username, password,filename;
                boolean fileReadAndWritable = false;

                if(user != null)
                    System.out.println("Menu - You can upload, download, modify (update and delete), list (all files), or logout");
                else
                    System.out.println("Menu - You can register or login");
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();

                switch (input) {
                    case "register":
                        if(user != null){
                            System.out.println("You need to log out to register");
                            break;
                        }
                        username = enterInput("Enter username:");
                        while(fileCatalogServer.notValidUsername(username)) {
                            username = enterInput("Username already exists, enter a new username");
                        }
                        password = enterInput("Enter password:");
                        fileCatalogServer.register(username, password);
                        System.out.println("Registered!");
                        break;

                    case "login":
                        if(user != null){
                            System.out.println("You are already logged in");
                            break;
                        }
                        username = enterInput("Enter username:");
                        password = enterInput("Enter password:");
                        user = fileCatalogServer.login(username, password, myRemoteObject);
                        if(user != null)
                            System.out.println("Logged in as " + user.getUsername());
                        else
                            System.out.println("Failed to log in, returning to menu");
                        break;

                    case "logout":
                        if(user != null){
                            user = null;
                            System.out.println("Logged out!");
                        }
                        else
                            System.out.println("Already logged out!");
                        break;

                    case "upload":
                        if(user == null){
                            System.out.println("You need to be logged in to be able to upload a file");
                            break;
                        }
                        filename = enterInput("Enter the name of the file you want to upload: ");
                        String temp  = enterInput("Do you want the file to be writable for others? y/n");
                        if(temp.equals("y"))
                            fileReadAndWritable = true;
                        fileCatalogServer.upload(filename, user, fileReadAndWritable, myRemoteObject);
                        break;

                    case "download":
                        if(user == null){
                            System.out.println("You need to be logged in to be able to download a file");
                            break;
                        }
                        filename = enterInput("Enter the name of the file you want to download: ");
                        File downloadedFile = fileCatalogServer.download(filename,user);
                        if (downloadedFile == null)
                            System.out.println("No file to download with that filename");
                        else {
                            System.out.println("Download successful");
                            System.out.println("Filename: " + downloadedFile.getFilename() + " Owner: " + downloadedFile.getOwner().getUsername() +
                                    " Size: " + downloadedFile.getSize() + " Writable: " + downloadedFile.readAndWritable());
                        }
                        break;

                    case "modify":
                        if(user == null){
                            System.out.println("You need to be logged in to be able to modify a file");
                            break;
                        }
                        filename = enterInput("Enter the name of the file you want to modify: ");
                        String action = enterInput("Do you want to delete or update the file?");
                        if(action.equals("update"))
                            action = enterInput("Do you want the file to be writable or only readable for others?");
                        fileCatalogServer.modify(filename,user,action);
                        break;

                    case "list":
                        if(user == null){
                            System.out.println("You need to be logged in to be able to list all files");
                            break;
                        }
                        List<? extends FileDTO> files = fileCatalogServer.listAllFiles();
                        System.out.println("Filename - Owner - Size - Writable");
                        for (FileDTO file : files) {
                            System.out.println((file.getFilename() + " - " + file.getOwner().getUsername() +
                                    " - " + file.getSize() + " - " + file.readAndWritable()));
                        }
                        break;

                    default:
                        System.out.println("Not a valid input");
                }
            } catch (IllegalArgumentException | RemoteException | SQLException  e) {
                e.printStackTrace();
            }
        }
    }

    public String enterInput (String info) {
        System.out.println(info);
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        return input;
    }

    private class ServerMessages extends UnicastRemoteObject implements FileCatalogClient {

        public ServerMessages() throws RemoteException {
        }

        @Override
        public void receiveMessage(String msg) {
            System.out.println(msg);
        }
    }
}
