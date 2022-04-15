package io.github.initauther97.nugget.file;

import io.github.initauther97.nugget.file.type.FileType;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileManager{

    private final Path root;
    private final FileSystem fs;
    private final FileManager parent;

    public FileManager(Path rt) {
        root = rt;
        fs = rt.getFileSystem();
        parent = null;
    }

    public FileManager(FileManager parent, Path relatedRoot) {
        root = relatedRoot;
        fs = root.getFileSystem();
        this.parent = parent;
    }

    public Path resolve(Path pth) {
        return getRoot().resolve(pth);
    }

    public Path resolve(String pth) {
        return getRoot().resolve(pth);
    }

    public Path resolve(String... parts) {
        return getRoot().resolve(String.join("/", parts));
    }

    public boolean exists(Path pth) {
        return Files.exists(resolve(pth));
    }

    public boolean exists(String pth) {
        return Files.exists(resolve(pth));
    }

    public boolean exists(String... pth) {
        return Files.exists(resolve(pth));
    }

    public AccessPermission checkPermission(Path pth) {
        Path path = resolve(pth);
        boolean readable = Files.isReadable(path);
        boolean writeable = Files.isWritable(path);
        if(writeable && readable) {
            return AccessPermission.READ_WRITE;
        } else if(writeable) {
            return AccessPermission.WRITE;
        } else if(readable) {
            return AccessPermission.READ;
        }
        return AccessPermission.DENIED;
    }

    public AccessPermission checkPermission(String pth) {
        Path path = resolve(pth);
        boolean readable = Files.isReadable(path);
        boolean writeable = Files.isWritable(path);
        if(writeable && readable) {
            return AccessPermission.READ_WRITE;
        } else if(writeable) {
            return AccessPermission.WRITE;
        } else if(readable) {
            return AccessPermission.READ;
        }
        return AccessPermission.DENIED;
    }

    public AccessPermission checkPermission(String... pth) {
        Path path = resolve(pth);
        boolean readable = Files.isReadable(path);
        boolean writeable = Files.isWritable(path);
        if(writeable && readable) {
            return AccessPermission.READ_WRITE;
        } else if(writeable) {
            return AccessPermission.WRITE;
        } else if(readable) {
            return AccessPermission.READ;
        }
        return AccessPermission.DENIED;
    }

    public<T> T visit(FileType<T> type, Path pth) throws IOException {
        Path path = resolve(pth);
        if(!Files.exists(path)) {
            VisitOperation vst = type.createIfNotExist(this, path);
            if (vst == VisitOperation.TERMINATE) {
                return null;
            }
        }
        return type.onVisit(this, path);
    }

    public<T> T visit(FileType<T> type, String pth) throws IOException {
        Path path = resolve(pth);
        if(!Files.exists(path)) {
            VisitOperation vst = type.createIfNotExist(this, path);
            if (vst == VisitOperation.TERMINATE) {
                return null;
            }
        }
        return type.onVisit(this, path);
    }

    public<T> T visit(FileType<T> type, String... pth) throws IOException {
        Path path = resolve(pth);
        if(!Files.exists(path)) {
            VisitOperation vst = type.createIfNotExist(this, path);
            if (vst == VisitOperation.TERMINATE) {
                return null;
            }
        }
        return type.onVisit(this, path);
    }

    public FileManager getParent() {
        return parent;
    }

    public Path getRoot() {
        return parent != null ? parent.resolve(root) : root;
    }
}
