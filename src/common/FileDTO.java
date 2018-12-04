package common;

import server.model.User;

import java.io.Serializable;

public interface FileDTO extends Serializable {
    String getFilename();
    User getOwner();
    int getSize();
    boolean readAndWritable();
}
