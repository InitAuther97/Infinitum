package io.github.initauther97.nugget.file.type;

import io.github.initauther97.nugget.file.FileManager;
import io.github.initauther97.nugget.file.VisitOperation;

import java.io.IOException;
import java.nio.file.Path;

public interface FileType<T> {

    VisitOperation createIfNotExist(FileManager manager, Path pth) throws IOException;

    boolean isType(FileManager manager, Path pth) throws IOException;

    default boolean canVisit(FileManager manager, Path pth) throws IOException {
        return manager.checkPermission(pth).isReadable();
    }

    T onVisit(FileManager manager, Path pth) throws IOException;
}
