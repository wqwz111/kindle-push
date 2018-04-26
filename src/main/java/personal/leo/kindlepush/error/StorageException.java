package personal.leo.kindlepush.error;

import java.io.IOException;

public class StorageException extends IOException {
    public StorageException(String msg) {
        super(msg);
    }

    public StorageException(Throwable e) {
        super(e);
    }

    public StorageException(String msg, Throwable e) {
        super(msg, e);
    }
}
