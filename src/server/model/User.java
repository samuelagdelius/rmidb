package server.model;

import common.FileCatalogClient;
import common.UserDTO;

public class User implements UserDTO{
    private String username;
    private FileCatalogClient fileCatalogClient;

    public User(String username){
        this.username = username;
    }

    public User(String username, FileCatalogClient fileCatalogClient){
        this.username = username;
        this.fileCatalogClient = fileCatalogClient;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
