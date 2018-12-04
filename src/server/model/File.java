package server.model;

import common.FileDTO;

public class File implements FileDTO {
    private String filename;
    private User owner;
    private boolean readAndWritable;
    private int size;

    public File (String filename, int size, User owner, boolean readAndWritable) {
        this.filename = filename;
        this.size = size;
        this.owner = owner;
        this.readAndWritable = readAndWritable;
    }

    public String getFilename() {
        return filename;
    }
    public User getOwner() {
        return owner;
    }

    public int getSize() {
        return size;
    }

    public boolean readAndWritable() {
        return readAndWritable;
    }
}
